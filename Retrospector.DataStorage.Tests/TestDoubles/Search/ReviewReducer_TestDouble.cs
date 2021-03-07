using System.Diagnostics.CodeAnalysis;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;

namespace Retrospector.DataStorage.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class ReviewReducer_TestDouble : Reducer_TestDouble<ReviewEntity>, IReviewReducer
    { }
}