using System.Collections.Generic;
using Retrospector.Core.Boundary;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Models;
using Retrospector.Core.Tests.TestDoubles.Search;
using Retrospector.Core.Tests.Utilities;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Search
{
    public class SearchUseCaseTests
    {
        private readonly IUseCase<SearchRequest> _useCase;
        private SearchPresenter_TestDouble _presenter;
        private SearchDataGateway_TestDouble _dataGateway;
        private QueryBuilder_TestDouble _builder;

        public SearchUseCaseTests()
        {
            _presenter = new SearchPresenter_TestDouble();
            _dataGateway = new SearchDataGateway_TestDouble();
            _builder = new QueryBuilder_TestDouble();
            _useCase = new SearchUseCase(_presenter, _builder, _dataGateway);
        }

        [Fact]
        public void builds_the_query()
        {
            var request = new SearchRequest{Query = "search"};

            _useCase.Execute(request);

            Assert.Equal(Verify.Once, _builder.CountOf_BuildQuery_Calls);
            Assert.Equal(request.Query, _builder.LastQueryPassedTo_BuildQuery);
        }

        [Fact]
        public void queries_the_data_gateway()
        {
            var tree = new QueryTree();
            _builder.ReturnFor_BuildQuery = tree;

            _useCase.Execute(new SearchRequest());

            Assert.Equal(Verify.Once, _dataGateway.CountOf_Search_Calls);
            Assert.Equal(tree, _dataGateway.LastQueryPassedTo_Search);
        }

        [Fact]
        public void informs_the_presenter()
        {
            var searchResults = new[] {new Dictionary<RetrospectorAttribute, string>()};
            _dataGateway.ReturnFor_Search = searchResults;

            _useCase.Execute(new SearchRequest());

            Assert.Equal(Verify.Once, _presenter.CountOf_Searched_Calls);
            Assert.Equal(searchResults, _presenter.LastResultsPassedTo_Search);
        }
    }
}