using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage.Models;

namespace Retrospector.Search.Interfaces
{
    public interface IReviewReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(Review item);
    }
}