using System;
using System.Collections.Generic;
using Retrospector.Main;
using Retrospector.Search;
using Retrospector.Search.Models;
using Retrospector.Tests.TestDoubles.Search;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Main
{
    public class MainWindowViewModelTests
    {
        private readonly MainWindowViewModel _viewModel;
        private readonly SearchDataGateway_TestDouble _searchDataGateway;
        private readonly QueryBuilder_TestDouble _queryBuilder;

        protected MainWindowViewModelTests()
        {
            _queryBuilder = new QueryBuilder_TestDouble();
            _searchDataGateway = new SearchDataGateway_TestDouble();
            _viewModel = new MainWindowViewModel(_queryBuilder, _searchDataGateway);
        }

        public class SearchCommand : MainWindowViewModelTests
        {
            public SearchCommand()
            {
                _searchDataGateway.ReturnFor_Search = Array.Empty<Dictionary<RetrospectorAttribute, string>>();
            }

            [Fact]
            public void creates_query_tree()
            {
                var search = "`U=Ben";

                _viewModel.SearchCommand.Execute(search);

                Assert.Equal(Verify.Once, _queryBuilder.CountOf_BuildQuery_Calls);
                Assert.Equal(search, _queryBuilder.LastQueryPassedTo_BuildQuery);
            }

            [Fact]
            public void searches_the_database()
            {
                var tree = new QueryTree();
                _queryBuilder.ReturnFor_BuildQuery = tree;

                _viewModel.SearchCommand.Execute("");

                Assert.Equal(Verify.Once, _searchDataGateway.CountOfCallsTo_Search);
                Assert.Equal(tree, _searchDataGateway.LastQueryPassedTo_Search);
            }

            [Fact]
            public void stores_results_on_property()
            {
                var result = new Dictionary<RetrospectorAttribute, string>();
                _searchDataGateway.ReturnFor_Search = new []{result};

                _viewModel.SearchCommand.Execute("");

                Assert.Contains(result, _viewModel.SearchResults);
            }

            [Fact]
            public void handles_wrong_type()
            {
                _viewModel.SearchCommand.Execute(DateTime.Now);
            }

            [Fact]
            public void handles_null()
            {
                _viewModel.SearchCommand.Execute(null);
            }

            [Fact]
            public void clears_old_search_results()
            {
                _viewModel.SearchResults.Add(new Dictionary<RetrospectorAttribute, string>());

                _viewModel.SearchCommand.Execute("");

                Assert.Empty(_viewModel.SearchResults);
            }
        }
    }
}