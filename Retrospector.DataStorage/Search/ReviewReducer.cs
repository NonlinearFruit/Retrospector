using System.Collections.Generic;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search.Interfaces;

namespace Retrospector.DataStorage.Search
{
    public class ReviewReducer : IReviewReducer
    {
        public Dictionary<RetrospectorAttribute, string> Reduce(ReviewEntity item)
        {
            return new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.ReviewDate, $"{item?.Date}" ?? ""},
                {RetrospectorAttribute.ReviewUser, item?.User ?? ""},
                {RetrospectorAttribute.ReviewRating, $"{item?.Rating}" ?? ""},
                {RetrospectorAttribute.ReviewContent, item?.Content ?? ""}
            };
        }
    }
}