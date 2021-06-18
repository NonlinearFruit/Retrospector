using System.Collections.Generic;
using System.Collections.ObjectModel;
using Retrospector.MediaTab.Interfaces;
using Retrospector.SearchTab.Interfaces;

namespace Retrospector.Main
{
    public class MainWindowViewModel
    {
        public ICollection<object> Tabs { get; }

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