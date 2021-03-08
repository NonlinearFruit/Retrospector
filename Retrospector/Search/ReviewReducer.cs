using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;

namespace Retrospector.Search
{
    public class ReviewReducer : IReviewReducer
    {
        public Dictionary<RetrospectorAttribute, string> Reduce(Review item)
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