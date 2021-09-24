using System;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.MediaTab;
using Retrospector.Setup;
using Retrospector.Tests.TestDoubles;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.MediaTab
{
    public class MediaTabViewModelTests : DatabaseContextTests
    {
        private MediaTabViewModel _viewModel;
        private readonly Factory_TestDouble<IDatabaseContext> _factory;
        private readonly Configuration _config;

        protected MediaTabViewModelTests()
        {
            _config = new Configuration
            {
                Categories = new[] { "Movie", "Tv Show" }
            };
            _factory = new Factory_TestDouble<IDatabaseContext>
            {
                ReturnFor_Factory = _actContext
            };
            _viewModel = new MediaTabViewModel(_config, _factory.Factory);
        }

        public class Constructor : MediaTabViewModelTests
        {
            [Fact]
            public void sets_header()
            {
                Assert.Equal("Media", _viewModel.Header);
            }

            [Fact]
            public void sets_categories()
            {
                Assert.Equal(_config.Categories, _viewModel.Categories);
            }

            [Fact]
            public void sets_media_types()
            {
                Assert.Equal(Enum.GetValues<MediaType>(), _viewModel.MediaTypes);
            }
        }

        public class New : MediaTabViewModelTests
        {
            [Fact]
            public void sets_the_media_property()
            {
                _viewModel.New();

                Assert.NotNull(_viewModel.Media);
            }
        }

        public class Load : MediaTabViewModelTests
        {
            [Fact]
            public void creates_database_context()
            {
                _viewModel.Load(0);

                Assert.Equal(Verify.Once, _factory.CountOfCallsTo_Factory);
            }

            [Fact]
            public void sets_media_property()
            {
                var media = new Media { Title = "Something Classic" };
                var id = _assertContext.Media.Add(media).Entity.Id;
                _assertContext.SaveChanges();

                _viewModel.Load(id);

                Assert.Equal(media.Title, _viewModel.Media.Title);
            }

            [Fact]
            public void loads_reviews()
            {
                var media = new Media {Reviews = new []
                {
                    new Review()
                }};
                var id = _assertContext.Media.Add(media).Entity.Id;
                _assertContext.SaveChanges();

                _viewModel.Load(id);

                Assert.NotEmpty(_viewModel.Media.Reviews);
            }

            [Fact]
            public void loads_factoids()
            {
                var media = new Media {Factoids = new []
                {
                    new Factoid()
                }};
                var id = _assertContext.Media.Add(media).Entity.Id;
                _assertContext.SaveChanges();

                _viewModel.Load(id);

                Assert.NotEmpty(_viewModel.Media.Factoids);
            }

            [Fact]
            public void does_not_load_wrong_media()
            {
                var media = new Media();
                var id = _assertContext.Media.Add(media).Entity.Id;
                _assertContext.SaveChanges();

                _viewModel.Load(id+1);

                Assert.Null(_viewModel.Media);
            }
        }
    }
}