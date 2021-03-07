using System.Linq;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Search
{
    public class SearchComponentTests : DatabaseContextTests
    {
        private ISearchDataGateway _dataGateway;

        public SearchComponentTests()
        {
            _dataGateway = new SearchDataGateway(
                new MediaReducer(),
                new ReviewReducer(),
                new FactoidReducer(),
                _actContext,
                new SearchFilterBuilder(new LeafExpressionBuilder()));
        }

        [Fact]
        public void filter_removes_when_media_title_does_not_match()
        {
            var title = "Best Movie";
            _arrangeContext.Media.Add(new MediaEntity{Title = title});
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.And, Leaves = new []{new QueryLeaf{Attribute = RetrospectorAttribute.MediaTitle, Comparator = Comparator.Equal, SearchValue = "not "+title}}};

            var results = _dataGateway.Search(query);

            Assert.Empty(results);
        }

        [Fact]
        public void filter_keeps_when_media_title_does_match()
        {
            var title = "Best Movie";
            _arrangeContext.Media.Add(new MediaEntity{Title = title});
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.And, Leaves = new []{new QueryLeaf{Attribute = RetrospectorAttribute.MediaTitle, Comparator = Comparator.Equal, SearchValue = title}}};

            var results = _dataGateway.Search(query);

            Assert.NotEmpty(results);
        }

        [Theory]
        [InlineDatas(0, 1, 10)]
        public void gets_all_media_correctly(int mediaCount)
        {
            _arrangeContext.Media.AddRange(mediaCount, i => new MediaEntity());
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.GiveMeEverything};

            var results = _dataGateway.Search(query);

            Assert.Equal(mediaCount, results.Count());
        }

        [Fact]
        public void gets_all_combinations_of_factoids_and_reviews()
        {
            var factoidCount = 5;
            var reviewCount = 10;
            var mediaId = _arrangeContext.Media.Add(new MediaEntity()).Entity.Id;
            _arrangeContext.Factoids.AddRange(factoidCount, i => new FactoidEntity{MediaId = mediaId});
            _arrangeContext.Reviews.AddRange(reviewCount, i => new ReviewEntity{MediaId = mediaId});
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.GiveMeEverything};

            var results = _dataGateway.Search(query);

            Assert.Equal(factoidCount*reviewCount, results.Count());
        }
    }
}