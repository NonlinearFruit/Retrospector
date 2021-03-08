using System;
using System.Collections.Generic;
using System.Linq;
using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Tests.TestDoubles.DataStorage;
using Retrospector.Tests.TestDoubles.Search;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Search
{
    public class SearchDataGatewayTests : IDisposable
    {
        private ISearchDataGateway _gateway;
        private DatabaseContext_TestDouble _arrangeContext;
        private DatabaseContext_TestDouble _actContext;
        private DatabaseContext_TestDouble _assertContext;
        private MediaReducer_TestDouble _mediaReducer;
        private ReviewReducer_TestDouble _reviewReducer;
        private FactoidReducer_TestDouble _factoidReducer;
        private SearchFilterBuilder_TestDouble _filterBuilder;

        public SearchDataGatewayTests()
        {
            var id = Guid.NewGuid().ToString();
            _arrangeContext = new DatabaseContext_TestDouble(id);
            _actContext = new DatabaseContext_TestDouble(id);
            _assertContext = new DatabaseContext_TestDouble(id);
            _mediaReducer = new MediaReducer_TestDouble();
            _reviewReducer = new ReviewReducer_TestDouble();
            _factoidReducer = new FactoidReducer_TestDouble();
            _filterBuilder = new SearchFilterBuilder_TestDouble();
            _gateway = new SearchDataGateway(_mediaReducer, _reviewReducer, _factoidReducer, _actContext, _filterBuilder);

            _mediaReducer.ReturnFor_Reduce = new Dictionary<RetrospectorAttribute, string>();
            _reviewReducer.ReturnFor_Reduce = new Dictionary<RetrospectorAttribute, string>();
            _factoidReducer.ReturnFor_Reduce = new Dictionary<RetrospectorAttribute, string>();
            _filterBuilder.ReturnFor_BuildExpression = _filterBuilder.Filter;
        }

        [Fact]
        public void empty_results_when_query_is_null()
        {
            var results = _gateway.Search(null);

            Assert.Empty(results);
        }

        [Fact]
        public void builds_expression_for_filtering()
        {
            var tree = new QueryTree();

            _gateway.Search(tree);

            Assert.Equal(Verify.Once, _filterBuilder.CountOf_BuildExpression_Calls);
            Assert.Equal(tree, _filterBuilder.LastTreePassedTo_BuildExpression);
        }

        [Fact]
        public void calls_filter_with_media()
        {
            var tree = new QueryTree();
            var title = "Sign of Four";
            var fact = "Genre";
            var rating = 8;
            var mediaId = ArrangeMedia(new Media{Title = title}).Id;
            ArrangeReview(new Review {Rating = rating}, mediaId);
            ArrangeFactoid(new Factoid {Title = fact}, mediaId);


            _gateway.Search(tree);

            Assert.Equal(Verify.Once, _filterBuilder.CountOf_Filter_Calls);
            Assert.Equal(title, _filterBuilder.LastMediaPassedTo_Filter.Title);
            Assert.Equal(rating, _filterBuilder.LastReviewPassedTo_Filter.Rating);
            Assert.Equal(fact, _filterBuilder.LastFactoidPassedTo_Filter.Title);
        }

        [Fact]
        public void returns_no_results_when_nothing_passes_the_filter()
        {
            ArrangeMedia(new Media());
            _filterBuilder.ReturnFor_Filter = false;

            var results = _gateway.Search(new QueryTree());

            Assert.Empty(results);
        }

        [Fact]
        public void reduces_media_when_media_match_is_found()
        {
            var id = ArrangeMedia(new Media()).Id;
            _filterBuilder.ReturnFor_Filter = true;

            _gateway.Search(new QueryTree());

            Assert.Equal(Verify.Once, _mediaReducer.CountOf_Reduce_Calls);
            Assert.Equal(id, _mediaReducer.LastItemPassedTo_Reduce.Id);
        }

        [Fact]
        public void reduces_review_when_review_match_is_found()
        {
            var id = ArrangeReview(new Review()).Id;
            _filterBuilder.ReturnFor_Filter = true;

            _gateway.Search(new QueryTree());

            Assert.Equal(Verify.Once, _reviewReducer.CountOf_Reduce_Calls);
            Assert.Equal(id, _reviewReducer.LastItemPassedTo_Reduce.Id);
        }

        [Fact]
        public void reduces_factoid_when_factoid_match_is_found()
        {
            var id = ArrangeFactoid(new Factoid()).Id;
            _filterBuilder.ReturnFor_Filter = true;

            _gateway.Search(new QueryTree());

            Assert.Equal(Verify.Once, _factoidReducer.CountOf_Reduce_Calls);
            Assert.Equal(id, _factoidReducer.LastItemPassedTo_Reduce.Id);
        }

        [Fact]
        public void returns_reduced_media_dictionary()
        {
            ArrangeMedia(new Media());
            _filterBuilder.ReturnFor_Filter = true;
            var dictionary = new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.MediaTitle, "Sign of Four"}
            };
            _mediaReducer.ReturnFor_Reduce = dictionary;

            var results = _gateway.Search(new QueryTree());

            Assert.Contains(dictionary, results);
        }

        [Fact]
        public void returns_reduced_review_dictionary()
        {
            ArrangeReview(new Review());
            _filterBuilder.ReturnFor_Filter = true;
            var dictionary = new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.ReviewRating, "8"}
            };
            _reviewReducer.ReturnFor_Reduce = dictionary;

            var results = _gateway.Search(new QueryTree());

            Assert.Contains(dictionary, results);
        }

        [Fact]
        public void returns_reduced_factoid_dictionary()
        {
            ArrangeFactoid(new Factoid());
            _filterBuilder.ReturnFor_Filter = true;
            var dictionary = new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.FactoidTitle, "Genre"}
            };
            _factoidReducer.ReturnFor_Reduce = dictionary;

            var results = _gateway.Search(new QueryTree());

            Assert.Contains(dictionary, results);
        }

        [Theory]
        [InlineDatas(0, 1, 10)]
        public void reduces_multiple_media_when_matches_are_found(int countOfMatches)
        {
            for (var i = 0; i < countOfMatches; i++)
                ArrangeMedia(new Media {Title = "Title"});
            var query = new QueryTree();
            _filterBuilder.ReturnFor_Filter = true;

            _gateway.Search(query);

            Assert.Equal(countOfMatches, _mediaReducer.CountOf_Reduce_Calls);
        }

        [Theory]
        [InlineDatas(0, 1, 10)]
        public void reduces_multiple_reviews_when_matches_are_found(int countOfMatches)
        {
            var mediaId = ArrangeMedia().Id;
            for (var i = 0; i < countOfMatches; i++)
                ArrangeReview(new Review {Rating = 5}, mediaId);
            var query = new QueryTree();
            _filterBuilder.ReturnFor_Filter = true;

            _gateway.Search(query);

            Assert.Equal(countOfMatches, _reviewReducer.CountOf_Reduce_Calls);
        }

        [Theory]
        [InlineDatas(0, 1, 10)]
        public void reduces_multiple_factoids_when_matches_are_found(int countOfMatches)
        {
            var mediaId = ArrangeMedia().Id;
            for (var i = 0; i < countOfMatches; i++)
                ArrangeFactoid(new Factoid {Title = "Title"}, mediaId);
            var query = new QueryTree();
            _filterBuilder.ReturnFor_Filter = true;

            _gateway.Search(query);

            Assert.Equal(countOfMatches, _factoidReducer.CountOf_Reduce_Calls);
        }

        [Theory]
        [InlineData(0, 0, 1)]
        [InlineData(1, 0, 1)]
        [InlineData(0, 1, 1)]
        [InlineData(1, 1, 1)]
        [InlineData(2, 0, 2)]
        [InlineData(2, 1, 2)]
        [InlineData(2, 2, 4)]
        [InlineData(1, 2, 2)]
        [InlineData(0, 2, 2)]
        [InlineData(4, 4, 16)]
        public void correct_number_of_results_for_a_media_based_on_its_reviews_and_factoids(
            int numberOfReviews,
            int numberOfFactoids,
            int resultCount)
        {
            var mediaId = ArrangeMedia().Id;
            for (var i = 0; i < numberOfFactoids; i++)
                ArrangeFactoid(new Factoid {Title = "Sign of Four"}, mediaId);
            for (var i = 0; i < numberOfReviews; i++)
                ArrangeReview(new Review {Rating = 9}, mediaId);
            var query = new QueryTree();
            _filterBuilder.ReturnFor_Filter = true;

            var results = _gateway.Search(query);

            Assert.Equal(resultCount, results.Count());
        }

        [Theory]
        [InlineData(0, 0)]
        [InlineData(1, 0)]
        [InlineData(0, 1)]
        [InlineData(1, 1)]
        [InlineData(2, 0)]
        [InlineData(2, 1)]
        [InlineData(2, 2)]
        [InlineData(1, 2)]
        [InlineData(0, 2)]
        [InlineData(4, 4)]
        public void single_result_for_a_media_even_with_orphaned_reviews_and_factoids(
            int numberOfReviews,
            int numberOfFactoids)
        {
            var mediaId = ArrangeMedia().Id;
            for (var i = 0; i < numberOfFactoids; i++)
                ArrangeFactoid(new Factoid {Title = "Sign of Four"}, mediaId+1);
            for (var i = 0; i < numberOfReviews; i++)
                ArrangeReview(new Review {Rating = 9}, mediaId+1);
            var query = new QueryTree();
            _filterBuilder.ReturnFor_Filter = true;

            var results = _gateway.Search(query);

            Assert.Single(results);
        }

        private Media ArrangeMedia(Media media = null)
        {
            var storedMedia = _arrangeContext.Media.Add(media ?? new Media()).Entity;
            _arrangeContext.SaveChanges();
            return storedMedia;
        }

        private Review ArrangeReview(Review review, int? mediaId = null)
        {
            review.MediaId = mediaId ?? ArrangeMedia().Id;
            var storedReview = _arrangeContext.Reviews.Add(review).Entity;
            _arrangeContext.SaveChanges();
            return storedReview;
        }

        private Factoid ArrangeFactoid(Factoid factoid, int? mediaId = null)
        {
            factoid.MediaId = mediaId ?? ArrangeMedia().Id;
            var storedFactoid = _arrangeContext.Factoids.Add(factoid).Entity;
            _arrangeContext.SaveChanges();
            return storedFactoid;
        }

        public void Dispose()
        {
            _arrangeContext?.Dispose();
            _actContext?.Dispose();
            _assertContext?.Dispose();
        }
    }
}