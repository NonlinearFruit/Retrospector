using System.Collections.Generic;
using System.Collections.ObjectModel;
using Retrospector.MediaTab.Interfaces;
using Retrospector.Search.Interfaces;
using Retrospector.SearchTab.Interfaces;

namespace Retrospector.Main
{
    public class MainWindowViewModel
    {
        private readonly IQueryBuilder _queryBuilder;
        private readonly ISearchDataGateway _searchDataGateway;

        public ICollection<object> Tabs { get; set; }

        public MainWindowViewModel(ISearchTab searchTab, IMediaTab mediaTab)
        {
            Tabs = new ObservableCollection<object>
            {
                searchTab,
                mediaTab
            };
        }
    }
}