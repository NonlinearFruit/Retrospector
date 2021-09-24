using Retrospector.Main;
using Retrospector.Tests.TestDoubles;
using Xunit;

namespace Retrospector.Tests.Tests.Main
{
    public class MainWindowViewModelTests
    {
        private MainWindowViewModel _viewModel;
        private SearchTab_TestDouble _searchTab;
        private MediaTab_TestDouble _mediaTab;

        protected MainWindowViewModelTests()
        {
            _searchTab = new SearchTab_TestDouble();
            _mediaTab = new MediaTab_TestDouble();
            _viewModel = new MainWindowViewModel(_searchTab, _mediaTab);
        }

        public class Constructor : MainWindowViewModelTests
        {
            [Fact]
            public void search_tab_is_stored_as_a_tab()
            {
                Assert.Contains(_searchTab, _viewModel.Tabs);
            }

            [Fact]
            public void media_tab_is_stored_as_a_tab()
            {
                Assert.Contains(_mediaTab, _viewModel.Tabs);
            }
        }
    }
}