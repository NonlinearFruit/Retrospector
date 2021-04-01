using System;
using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.SearchTab;
using Retrospector.Tests.TestDoubles.Search;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.SearchTab
{
    public class SearchTabViewModelTests
    {
        private readonly SearchTabViewModel _viewModel;
        private readonly SearchDataGateway_TestDouble _searchDataGateway;
        private readonly QueryBuilder_TestDouble _queryBuilder;

        protected SearchTabViewModelTests()
        {
            _queryBuilder = new QueryBuilder_TestDouble();
            _searchDataGateway = new SearchDataGateway_TestDouble();
            _viewModel = new SearchTabViewModel(_queryBuilder, _searchDataGateway);
        }

        public class Constructor : SearchTabViewModelTests
        {
            [Fact]
            public void sets_header()
            {
                Assert.Equal("Search", _viewModel.Header);
            }
        }

        public class SearchCommand : SearchTabViewModelTests
        {
            public SearchCommand()
            {
                _searchDataGateway.ReturnFor_Search = Array.Empty<Dictionary<RetrospectorAttribute, string>>();
            }

            [Fact]
            public void creates_query_tree()
            {
                var search = "`U=Ben";
                _viewModel.SearchText = search;

                _viewModel.SearchCommand.Execute(null);

                Assert.Equal(Verify.Once, _queryBuilder.CountOf_BuildQuery_Calls);
                Assert.Equal(search, _queryBuilder.LastQueryPassedTo_BuildQuery);
            }

            [Fact]
            public void searches_the_database()
            {
                var tree = new QueryTree();
                _queryBuilder.ReturnFor_BuildQuery = tree;

                _viewModel.SearchCommand.Execute(null);

                Assert.Equal(Verify.Once, _searchDataGateway.CountOfCallsTo_Search);
                Assert.Equal(tree, _searchDataGateway.LastQueryPassedTo_Search);
            }

            [Fact]
            public void stores_results_on_property()
            {
                var result = new Dictionary<RetrospectorAttribute, string>();
                _searchDataGateway.ReturnFor_Search = new []{result};

                _viewModel.SearchCommand.Execute(null);

                Assert.Contains(result, _viewModel.SearchResults);
            }

            [Fact]
            public void handles_null()
            {
                _viewModel.SearchText = null;

                _viewModel.SearchCommand.Execute(null);
            }

            [Fact]
            public void clears_old_search_results()
            {
                _viewModel.SearchResults.Add(new Dictionary<RetrospectorAttribute, string>());

                _viewModel.SearchCommand.Execute(null);

                Assert.Empty(_viewModel.SearchResults);
            }
        }
    }
}