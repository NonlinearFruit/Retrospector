using Retrospector.MediaTab;
using Xunit;

namespace Retrospector.Tests.Tests.MediaTab
{
    public class MediaTabViewModelTests
    {
        private MediaTabViewModel _viewModel;

        protected MediaTabViewModelTests()
        {
            _viewModel = new MediaTabViewModel();
        }

        public class Load : MediaTabViewModelTests
        {
            [Fact]
            public void sets_header()
            {
                Assert.Equal("Media", _viewModel.Header);
            }
        }
    }
}