using System.Collections.Generic;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Reviews.Entities;

namespace Retrospector.DataStorage.Search.Interfaces
{
    public interface IReviewReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(ReviewEntity item);
    }
}