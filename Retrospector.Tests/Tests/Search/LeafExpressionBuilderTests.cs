using System;
using System.Collections.Generic;
using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Search
{
    public class LeafExpressionBuilderTests
    {
        private ILeafExpressionBuilder _builder;

        public LeafExpressionBuilderTests()
        {
            _builder = new LeafExpressionBuilder();
        }

        [Fact]
        public void build_handles_null()
        {
            var function = _builder.BuildExpression(null);

            Assert.NotNull(function);
        }

        [Fact]
        public void returns_valid_delegate()
        {
            var function = _builder.BuildExpression(new QueryLeaf());
            function.Compile().Invoke(new Media(), new Review(), new Factoid());
        }

        [Fact]
        public void delegate_handles_nulls()
        {
            var query = ArrangeQuery();

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, null, null);

            Assert.False(isMatch);
        }

        [Theory]
        [CombinationData(nameof(MediaAttributes), nameof(GreaterThanComparator), nameof(GreaterThanStringData))]
        [CombinationData(nameof(MediaAttributes), nameof(LessThanComparator), nameof(LessThanStringData))]
        [CombinationData(nameof(MediaAttributes), nameof(ContainsComparator), nameof(ContainsStringData))]
        [CombinationData(nameof(MediaAttributes), nameof(EqualComparator), nameof(EqualStringData))]
        public void creates_expression_that_filters_out_media(
            RetrospectorAttribute attribute,
            Comparator comparator,
            string actualValue,
            string searchValue,
            bool shouldMatch
            )
        {
            var query = ArrangeQuery(attribute, comparator, searchValue);
            var media = ArrangeMedia(attribute, actualValue);

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(media, null, null);

            Assert.Equal(shouldMatch, isMatch);
        }

        [Theory]
        [CombinationData(nameof(ReviewAttributes), nameof(GreaterThanComparator), nameof(GreaterThanStringData))]
        [CombinationData(nameof(ReviewAttributes), nameof(LessThanComparator), nameof(LessThanStringData))]
        [CombinationData(nameof(ReviewAttributes), nameof(ContainsComparator), nameof(ContainsStringData))]
        [CombinationData(nameof(ReviewAttributes), nameof(EqualComparator), nameof(EqualStringData))]
        public void creates_expression_that_filters_out_review(
            RetrospectorAttribute attribute,
            Comparator comparator,
            string actualValue,
            string searchValue,
            bool shouldMatch
            )
        {
            var query = ArrangeQuery(attribute, comparator, searchValue);
            var review = ArrangeReview(attribute, actualValue);

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, review, null);

            Assert.Equal(shouldMatch, isMatch);
        }

        [Theory]
        [InlineData(Comparator.LessThan, "2021-02-11", "2021-02-10", false)]
        [InlineData(Comparator.LessThan, "2021-02-11", "2021-02-12", true)]
        [InlineData(Comparator.GreaterThan, "2021-02-11", "2021-02-12", false)]
        [InlineData(Comparator.GreaterThan, "2021-02-11", "2021-02-10", true)]
        [InlineData(Comparator.Equal, "2021-02-11", "2021-02-11", true)]
        [InlineData(Comparator.Equal, "2021-02-11", "2021-03-11", false)]
        [InlineData(Comparator.Contains, "2021-02-11", "2021-02-11", true)]
        [InlineData(Comparator.Contains, "2021-02-11", "2021-03-11", false)]
        public void creates_expression_that_filters_out_review_based_on_date(Comparator comparator, string actualDate, string searchValue, bool shouldMatch)
        {
            var query = ArrangeQuery(RetrospectorAttribute.ReviewDate, comparator, searchValue);
            var review = new Review
            {
                Date = DateTime.Parse(actualDate)
            };

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, review, null);

            Assert.Equal(shouldMatch, isMatch);
        }

        [Theory]
        [InlineData(Comparator.LessThan)]
        [InlineData(Comparator.GreaterThan)]
        [InlineData(Comparator.Equal)]
        [InlineData(Comparator.Contains)]
        public void handles_non_integer_when_searching_on_review_date(Comparator comparator)
        {
            var query = ArrangeQuery(RetrospectorAttribute.ReviewDate, comparator, "not date");
            var review = new Review();

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, review, null);

            Assert.False(isMatch);
        }

        [Theory]
        [InlineData(Comparator.LessThan, 6, "5", false)]
        [InlineData(Comparator.LessThan, 5, "6", true)]
        [InlineData(Comparator.GreaterThan, 5, "6", false)]
        [InlineData(Comparator.GreaterThan, 6, "5", true)]
        [InlineData(Comparator.Equal, 5, "5", true)]
        [InlineData(Comparator.Equal, 5, "6", false)]
        [InlineData(Comparator.Contains, 5, "5", true)]
        [InlineData(Comparator.Contains, 5, "6", false)]
        public void creates_expression_that_filters_out_review_based_on_rating(Comparator comparator, int actualRating, string searchValue, bool shouldMatch)
        {
            var query = ArrangeQuery(RetrospectorAttribute.ReviewRating, comparator, searchValue);
            var review = new Review
            {
                Rating = actualRating
            };

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, review, null);

            Assert.Equal(shouldMatch, isMatch);
        }

        [Theory]
        [InlineData(Comparator.LessThan)]
        [InlineData(Comparator.GreaterThan)]
        [InlineData(Comparator.Equal)]
        [InlineData(Comparator.Contains)]
        public void handles_non_integer_when_searching_on_review_rating(Comparator comparator)
        {
            var query = ArrangeQuery(RetrospectorAttribute.ReviewRating, comparator, "not int");
            var review = new Review();

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, review, null);

            Assert.False(isMatch);
        }

        [Theory]
        [CombinationData(nameof(FactoidAttributes), nameof(GreaterThanComparator), nameof(GreaterThanStringData))]
        [CombinationData(nameof(FactoidAttributes), nameof(LessThanComparator), nameof(LessThanStringData))]
        [CombinationData(nameof(FactoidAttributes), nameof(ContainsComparator), nameof(ContainsStringData))]
        [CombinationData(nameof(FactoidAttributes), nameof(EqualComparator), nameof(EqualStringData))]
        public void creates_expression_that_filters_out_factoid(
            RetrospectorAttribute attribute,
            Comparator comparator,
            string actualValue,
            string searchValue,
            bool shouldMatch
            )
        {
            var query = ArrangeQuery(attribute, comparator, searchValue);
            var factoid = ArrangeFactoid(attribute, actualValue);

            var function = _builder.BuildExpression(query);
            var isMatch = function.Compile().Invoke(null, null, factoid);

            Assert.Equal(shouldMatch, isMatch);
        }

        public static IEnumerable<object[]> MediaAttributes = new[]
        {
            new object[] {RetrospectorAttribute.MediaTitle},
            new object[] {RetrospectorAttribute.MediaCreator},
            new object[] {RetrospectorAttribute.MediaSeason},
            new object[] {RetrospectorAttribute.MediaEpisode},
            new object[] {RetrospectorAttribute.MediaCategory},
            new object[] {RetrospectorAttribute.MediaDescription}
        };

        public static IEnumerable<object[]> ReviewAttributes = new[]
        {
            new object[] {RetrospectorAttribute.ReviewContent},
            new object[] {RetrospectorAttribute.ReviewUser}
        };

        public static IEnumerable<object[]> FactoidAttributes = new[]
        {
            new object[] {RetrospectorAttribute.FactoidTitle},
            new object[] {RetrospectorAttribute.FactoidContent}
        };

        public static IEnumerable<object[]> ContainsComparator = new[]
        {
            new object[] {Comparator.Contains}
        };

        public static IEnumerable<object[]> EqualComparator = new[]
        {
            new object[] {Comparator.Equal}
        };

        public static IEnumerable<object[]> GreaterThanComparator = new[]
        {
            new object[] {Comparator.GreaterThan}
        };

        public static IEnumerable<object[]> LessThanComparator = new[]
        {
            new object[] {Comparator.LessThan}
        };

        public static IEnumerable<object[]> EqualStringData = new[]
        {
            new object[] {"Equal", "Equal", true},
            new object[] {"EQUAL", "Equal", true},
            new object[] {"Equal", "EQUAL", true},
            new object[] {"Equal", "Not Equal", false},
            new object[] {"Not Equal", "Equal", false}
        };

        public static IEnumerable<object[]> ContainsStringData = new[]
        {
            new object[] {"Contains", "Con", true},
            new object[] {"CONTAINS", "Con", true},
            new object[] {"Contains", "CON", true},
            new object[] {"No Contains", "Yogurt", false}
        };

        public static IEnumerable<object[]> LessThanStringData = new[]
        {
            new object[] {"A", "B", false},
            new object[] {"a", "B", false},
            new object[] {"A", "b", false},
            new object[] {"B", "A", true},
            new object[] {"b", "A", true},
            new object[] {"B", "a", true}
        };

        public static IEnumerable<object[]> GreaterThanStringData = new[]
        {
            new object[] {"A", "B", true},
            new object[] {"a", "B", true},
            new object[] {"A", "b", true},
            new object[] {"B", "A", false},
            new object[] {"b", "A", false},
            new object[] {"B", "a", false}
        };

        private QueryLeaf ArrangeQuery(
            RetrospectorAttribute attribute = RetrospectorAttribute.FactoidContent,
            Comparator comparator = Comparator.Contains,
            string value = null
            ) => new QueryLeaf
                {
                    Attribute = attribute,
                    Comparator = comparator,
                    SearchValue = value
                };

        private Media ArrangeMedia(RetrospectorAttribute attribute, string value)
        {
            var media = new Media();
            switch (attribute)
            {
                case RetrospectorAttribute.MediaTitle:
                    media.Title = value;
                    break;
                case RetrospectorAttribute.MediaCreator:
                    media.Creator = value;
                    break;
                case RetrospectorAttribute.MediaSeason:
                    media.SeasonId = value;
                    break;
                case RetrospectorAttribute.MediaEpisode:
                    media.EpisodeId = value;
                    break;
                case RetrospectorAttribute.MediaCategory:
                    media.Category = value;
                    break;
                case RetrospectorAttribute.MediaDescription:
                    media.Description = value;
                    break;
            }
            return media;
        }

        private Review ArrangeReview(RetrospectorAttribute attribute, string value)
        {
            var review = new Review();
            switch (attribute)
            {
                case RetrospectorAttribute.ReviewUser:
                    review.User = value;
                    break;
                case RetrospectorAttribute.ReviewContent:
                    review.Content = value;
                    break;
            }
            return review;
        }

        private Factoid ArrangeFactoid(RetrospectorAttribute attribute, string value)
        {
            var factoid = new Factoid();
            switch (attribute)
            {
                case RetrospectorAttribute.FactoidTitle:
                    factoid.Title = value;
                    break;
                case RetrospectorAttribute.FactoidContent:
                    factoid.Content = value;
                    break;
            }
            return factoid;
        }
    }
}