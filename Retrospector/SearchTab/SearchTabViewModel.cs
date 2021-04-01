using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows.Input;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.SearchTab.Interfaces;
using Retrospector.Utilities;

namespace Retrospector.SearchTab
{
    public class SearchTabViewModel : ISearchTab
    {
        private readonly IQueryBuilder _queryBuilder;
        private readonly ISearchDataGateway _searchDataGateway;

        public SearchTabViewModel(IQueryBuilder queryBuilder, ISearchDataGateway searchDataGateway)
        {
            _queryBuilder = queryBuilder;
            _searchDataGateway = searchDataGateway;
            SearchCommand = new DelegateCommand(Search);
            SearchResults = new ObservableCollection<IDictionary<RetrospectorAttribute, string>>();
        }

        public string SearchText { get; set; }
        public ICommand SearchCommand { get; set; }
        public ICollection<IDictionary<RetrospectorAttribute, string>> SearchResults { get; set; }
        public string Header { get; set; } = "Search";

        private void Search(object obj)
        {
            var tree = _queryBuilder.BuildQuery(SearchText);
            SearchResults.Clear();
            _searchDataGateway
                .Search(tree)
                .ToList()
                .ForEach(SearchResults.Add);
        }
    }
}