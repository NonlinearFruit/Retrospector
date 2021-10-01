using System;
using System.Linq;
using Retrospector.Main;
using Retrospector.Tests.TestDoubles.AchievementTab;
using Retrospector.Tests.TestDoubles.MediaTab;
using Retrospector.Tests.TestDoubles.SearchTab;
using Xunit;

namespace Retrospector.Tests.Tests.Main
{
    public class MainWindowViewModelTests
    {
        private MainWindowViewModel _viewModel;
        private readonly SearchTab_TestDouble _searchTab;
        private readonly MediaTab_TestDouble _mediaTab;
        private readonly AchievementTab_TestDouble _achievementTab;

        protected MainWindowViewModelTests()
        {
            _searchTab = new SearchTab_TestDouble();
            _mediaTab = new MediaTab_TestDouble();
            _achievementTab = new AchievementTab_TestDouble();
            _viewModel = new MainWindowViewModel(_searchTab, _mediaTab, _achievementTab);
        }

        public class Constructor : MainWindowViewModelTests
        {
            [Theory]
            [InlineData(typeof(SearchTab_TestDouble))]
            [InlineData(typeof(MediaTab_TestDouble))]
            [InlineData(typeof(AchievementTab_TestDouble))]
            public void tab_is_stored_in_collection(Type tab)
            {
                Assert.Contains(tab, _viewModel.Tabs.Select(t => t.GetType()));
            }
        }
    }
}