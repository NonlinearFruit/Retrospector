using System.Collections.Generic;
using System.Collections.ObjectModel;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.Main.Interfaces;
using Retrospector.MediaTab.Interfaces;
using Retrospector.SearchTab.Interfaces;

namespace Retrospector.Main
{
    public class MainWindowViewModel
    {
        public ICollection<ITab> Tabs { get; }

        public MainWindowViewModel(
            ISearchTab searchTab,
            IMediaTab mediaTab,
            IAchievementTab achievementTab
        )
        {
            Tabs = new ObservableCollection<ITab>
            {
                searchTab,
                mediaTab,
                achievementTab
            };
        }
    }
}