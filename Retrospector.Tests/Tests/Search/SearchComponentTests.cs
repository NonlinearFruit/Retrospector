using System.Linq;
using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Search
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
            _arrangeContext.Media.Add(new Media{Title = title});
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.And, Leaves = new []{new QueryLeaf{Attribute = RetrospectorAttribute.MediaTitle, Comparator = Comparator.Equal, SearchValue = "not "+title}}};

            var results = _dataGateway.Search(query);

            Assert.Empty(results);
        }

        [Fact]
        public void filter_keeps_when_media_title_does_match()
        {
            var title = "Best Movie";
            _arrangeContext.Media.Add(new Media{Title = title});
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.And, Leaves = new []{new QueryLeaf{Attribute = RetrospectorAttribute.MediaTitle, Comparator = Comparator.Equal, SearchValue = title}}};

            var results = _dataGateway.Search(query);

            Assert.NotEmpty(results);
        }

        [Theory]
        [InlineDatas(0, 1, 10)]
        public void gets_all_media_correctly(int mediaCount)
        {
            _arrangeContext.Media.AddRange(mediaCount, i => new Media());
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
            var mediaId = _arrangeContext.Media.Add(new Media()).Entity.Id;
            _arrangeContext.Factoids.AddRange(factoidCount, i => new Factoid{MediaId = mediaId});
            _arrangeContext.Reviews.AddRange(reviewCount, i => new Review{MediaId = mediaId});
            _arrangeContext.SaveChanges();
            var query = new QueryTree {Type = OperatorType.GiveMeEverything};

            var results = _dataGateway.Search(query);

            Assert.Equal(factoidCount*reviewCount, results.Count());
        }
    }
}