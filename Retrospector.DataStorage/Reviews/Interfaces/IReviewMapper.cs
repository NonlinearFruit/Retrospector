using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Reviews.Entities;

namespace Retrospector.DataStorage.Reviews.Interfaces
{
    public interface IReviewMapper
    {
        Review ToModel(ReviewEntity entity);
        ReviewEntity ToEntity(Review model);
    }
}