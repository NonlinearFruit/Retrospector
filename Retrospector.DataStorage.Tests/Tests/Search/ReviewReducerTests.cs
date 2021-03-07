using System;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search;
using Retrospector.DataStorage.Search.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Search
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
        [InlineData(nameof(ReviewEntity.Rating), RetrospectorAttribute.ReviewRating, 6)]
        [InlineData(nameof(ReviewEntity.User), RetrospectorAttribute.ReviewUser, "Josh")]
        [InlineData(nameof(ReviewEntity.Content), RetrospectorAttribute.ReviewContent, "My review")]
        public void populates_the_attributes(string property, RetrospectorAttribute attribute, object value)
        {
            var review = new ReviewEntity();
            Reflection.SetProperty(review, property, value);

            var result = _reducer.Reduce(review);

            Assert.Equal($"{value}", result[attribute]);
        }

        [Fact]
        public void populates_the_datetime_attribute()
        {
            var review = new ReviewEntity
            {
                Date = DateTime.Now
            };

            var result = _reducer.Reduce(review);

            Assert.Equal($"{review.Date}", result[RetrospectorAttribute.ReviewDate]);
        }
    }
}