using System;
using System.Linq.Expressions;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Search.Interfaces;
using Filter = System.Func<Retrospector.DataStorage.Medias.Entities.MediaEntity, Retrospector.DataStorage.Reviews.Entities.ReviewEntity, Retrospector.DataStorage.Factoids.Entities.FactoidEntity, bool>;

namespace Retrospector.DataStorage.Search
{
    public class LeafExpressionBuilder : ILeafExpressionBuilder
    {
        private static Expression<Filter> FalseIfNulls = (m, r, f) => !(m == null && r == null && f == null);
        private static Expression<Filter> MatchEverything = (m, r, f) => true;
        private static Expression<Filter> MatchNothing = (m, r, f) => false;
        private static StringComparison DefaultComparison = StringComparison.CurrentCultureIgnoreCase;

        public Expression<Filter> BuildExpression(QueryLeaf leaf)
        {
            if (leaf == null)
                return MatchEverything;
            return leaf.Attribute switch
            {
                RetrospectorAttribute.MediaTitle => BuildMediaTitleCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.MediaCreator => BuildMediaCreatorCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.MediaSeason => BuildMediaSeasonCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.MediaEpisode => BuildMediaEpisodeCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.MediaCategory => BuildMediaCategoryCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.MediaDescription => BuildMediaDescriptionCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.ReviewUser => BuildReviewUserCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.ReviewContent => BuildReviewContentCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.ReviewRating => BuildReviewRatingCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.ReviewDate => BuildReviewDateCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.FactoidTitle => BuildFactoidTitleCheck(leaf.Comparator, leaf.SearchValue),
                RetrospectorAttribute.FactoidContent => BuildFactoidContentCheck(leaf.Comparator, leaf.SearchValue),
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildMediaTitleCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => m != null && m.Title != null && string.Equals(m.Title, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => m != null && m.Title != null && m.Title.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => m != null && m.Title != null && string.Compare(check, m.Title, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => m != null && m.Title != null && string.Compare(check, m.Title, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildMediaCreatorCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => m != null && m.Creator != null && string.Equals(m.Creator, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => m != null && m.Creator != null && m.Creator.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => m != null && m.Creator != null && string.Compare(check, m.Creator, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => m != null && m.Creator != null && string.Compare(check, m.Creator, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildMediaSeasonCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => m != null && m.SeasonId != null && string.Equals(m.SeasonId, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => m != null && m.SeasonId != null && m.SeasonId.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => m != null && m.SeasonId != null && string.Compare(check, m.SeasonId, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => m != null && m.SeasonId != null && string.Compare(check, m.SeasonId, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildMediaEpisodeCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => m != null && m.EpisodeId != null && string.Equals(m.EpisodeId, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => m != null && m.EpisodeId != null && m.EpisodeId.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => m != null && m.EpisodeId != null && string.Compare(check, m.EpisodeId, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => m != null && m.EpisodeId != null && string.Compare(check, m.EpisodeId, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildMediaDescriptionCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => m != null && m.Description != null && string.Equals(m.Description, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => m != null && m.Description != null && m.Description.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => m != null && m.Description != null && string.Compare(check, m.Description, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => m != null && m.Description != null && string.Compare(check, m.Description, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildMediaCategoryCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => m != null && m.Category != null && string.Equals(m.Category, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => m != null && m.Category != null && m.Category.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => m != null && m.Category != null && string.Compare(check, m.Category, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => m != null && m.Category != null && string.Compare(check, m.Category, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildReviewUserCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => r != null && r.User != null && string.Equals(r.User, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => r != null && r.User != null && r.User.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => r != null && r.User != null && string.Compare(check, r.User, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => r != null && r.User != null && string.Compare(check, r.User, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildReviewContentCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => r != null && r.Content != null && string.Equals(r.Content, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => r != null && r.Content != null && r.Content.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => r != null && r.Content != null && string.Compare(check, r.Content, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => r != null && r.Content != null && string.Compare(check, r.Content, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildReviewDateCheck(Comparator comparator, string check)
        {
            if (!DateTime.TryParse(check, out var date))
                return MatchNothing;
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => r != null && r.Date == date,
                Comparator.Contains    => (m, r, f) => r != null && r.Date == date,
                Comparator.GreaterThan => (m, r, f) => r != null && r.Date > date,
                Comparator.LessThan    => (m, r, f) => r != null && r.Date < date,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildReviewRatingCheck(Comparator comparator, string check)
        {
            if (!int.TryParse(check, out var rating))
                return MatchNothing;
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => r != null && r.Rating == rating,
                Comparator.Contains    => (m, r, f) => r != null && r.Rating == rating,
                Comparator.GreaterThan => (m, r, f) => r != null && r.Rating > rating,
                Comparator.LessThan    => (m, r, f) => r != null && r.Rating < rating,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildFactoidTitleCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => f != null && f.Title != null && string.Equals(f.Title, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => f != null && f.Title != null && f.Title.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => f != null && f.Title != null && string.Compare(check, f.Title, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => f != null && f.Title != null && string.Compare(check, f.Title, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }

        private static Expression<Filter> BuildFactoidContentCheck(Comparator comparator, string check)
        {
            return comparator switch
            {
                Comparator.Equal       => (m, r, f) => f != null && f.Content != null && string.Equals(f.Content, check, DefaultComparison),
                Comparator.Contains    => (m, r, f) => f != null && f.Content != null && f.Content.Contains(check, DefaultComparison),
                Comparator.GreaterThan => (m, r, f) => f != null && f.Content != null && string.Compare(check, f.Content, DefaultComparison) > 0,
                Comparator.LessThan    => (m, r, f) => f != null && f.Content != null && string.Compare(check, f.Content, DefaultComparison) < 0,
                _ => FalseIfNulls
            };
        }
    }
}