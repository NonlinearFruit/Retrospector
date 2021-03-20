using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows.Input;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Utilities;

namespace Retrospector.Main
{
    public class MainWindowViewModel
    {
        private readonly IQueryBuilder _queryBuilder;
        private readonly ISearchDataGateway _searchDataGateway;

        public string SearchText { get; set; }
        public ICommand SearchCommand { get; set; }
        public ICollection<IDictionary<RetrospectorAttribute, string>> SearchResults { get; set; }

        public MainWindowViewModel(IQueryBuilder queryBuilder, ISearchDataGateway searchDataGateway)
        {
            _queryBuilder = queryBuilder;
            _searchDataGateway = searchDataGateway;
            SearchCommand = new DelegateCommand(Search);
            SearchResults = new ObservableCollection<IDictionary<RetrospectorAttribute, string>>();
        }

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