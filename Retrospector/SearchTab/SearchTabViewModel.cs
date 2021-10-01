using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows.Input;
using Retrospector.MediaTab.Interfaces;
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
        private readonly IMediaTab _mediaTab;

        public SearchTabViewModel(IQueryBuilder queryBuilder, ISearchDataGateway searchDataGateway, IMediaTab mediaTab)
        {
            _queryBuilder = queryBuilder;
            _searchDataGateway = searchDataGateway;
            _mediaTab = mediaTab;
            SearchCommand = new DelegateCommand(Search);
            LoadMediaCommand = new DelegateCommand(LoadMedia);
            NewMediaCommand = new DelegateCommand(NewMedia);
            SearchResults = new ObservableCollection<IDictionary<RetrospectorAttribute, string>>();
        }

        public string SearchText { get; set; }
        public ICommand SearchCommand { get; set; }
        public ICommand LoadMediaCommand { get; set; }
        public ICommand NewMediaCommand { get; set; }
        public IDictionary<RetrospectorAttribute, string> SelectedResult { get; set; }
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

        private void NewMedia(object obj)
        {
            _mediaTab.New();
        }

        private void LoadMedia(object obj)
        {
            if (SelectedResult is not null && SelectedResult.ContainsKey(RetrospectorAttribute.MediaId) && int.TryParse(SelectedResult[RetrospectorAttribute.MediaId], out var mediaId))
                _mediaTab.Load(mediaId);
        }
    }
}