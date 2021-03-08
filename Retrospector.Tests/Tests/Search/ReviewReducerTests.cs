using System;
using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Search
{
    public class ReviewReducerTests
    {
        private IReviewReducer _reducer;

        public ReviewReducerTests()
        {
            _reducer = new ReviewReducer();
        }

        [Theory]
        [InlineData(RetrospectorAttribute.ReviewRating)]
        [InlineData(RetrospectorAttribute.ReviewUser)]
        [InlineData(RetrospectorAttribute.ReviewDate)]
        [InlineData(RetrospectorAttribute.ReviewContent)]
        public void returns_the_proper_attributes(RetrospectorAttribute attribute)
        {
            var result = _reducer.Reduce(null);

            Assert.Contains(attribute, result.Keys);
        }

        [Theory]
        [InlineData(RetrospectorAttribute.ReviewRating)]
        [InlineData(RetrospectorAttribute.ReviewUser)]
        [InlineData(RetrospectorAttribute.ReviewDate)]
        [InlineData(RetrospectorAttribute.ReviewContent)]
        public void defaults_to_empty_string(RetrospectorAttribute attribute)
        {
            var result = _reducer.Reduce(null);

            Assert.Equal("", result[attribute]);
        }

        [Theory]
        [InlineData(nameof(Review.Rating), RetrospectorAttribute.ReviewRating, 6)]
        [InlineData(nameof(Review.User), RetrospectorAttribute.ReviewUser, "Josh")]
        [InlineData(nameof(Review.Content), RetrospectorAttribute.ReviewContent, "My review")]
        public void populates_the_attributes(string property, RetrospectorAttribute attribute, object value)
        {
            var review = new Review();
            Reflection.SetProperty(review, property, value);

            var result = _reducer.Reduce(review);

            Assert.Equal($"{value}", result[attribute]);
        }

        [Fact]
        public void populates_the_datetime_attribute()
        {
            var review = new Review
            {
                Date = DateTime.Now
            };

            var result = _reducer.Reduce(review);

            Assert.Equal($"{review.Date}", result[RetrospectorAttribute.ReviewDate]);
        }
    }
}