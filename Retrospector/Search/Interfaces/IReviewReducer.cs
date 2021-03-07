using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage;

namespace Retrospector.Search.Interfaces
{
    public interface IReviewReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(ReviewEntity item);
    }
}