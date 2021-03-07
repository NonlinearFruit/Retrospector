using Retrospector.Core.Boundary;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search
{
    public class SearchUseCase : IUseCase<SearchRequest>
    {
        private readonly ISearchPresenter _presenter;
        private readonly IQueryBuilder _builder;
        private readonly ISearchDataGateway _dataGateway;

        public SearchUseCase(ISearchPresenter presenter, IQueryBuilder builder, ISearchDataGateway dataGateway)
        {
            _presenter = presenter;
            _builder = builder;
            _dataGateway = dataGateway;
        }

        public void Execute(SearchRequest request)
        {
            _presenter.Searched(_dataGateway.Search(_builder.BuildQuery(request.Query)));
        }
    }
}