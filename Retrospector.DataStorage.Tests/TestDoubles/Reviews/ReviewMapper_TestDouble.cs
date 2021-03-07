using System.Diagnostics.CodeAnalysis;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Reviews.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;

namespace Retrospector.DataStorage.Tests.TestDoubles.Reviews
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class ReviewMapper_TestDouble : Mapper_TestDouble<Review, ReviewEntity>, IReviewMapper
    { }
}