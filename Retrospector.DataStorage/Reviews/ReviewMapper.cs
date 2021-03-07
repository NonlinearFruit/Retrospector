using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Reviews.Interfaces;

namespace Retrospector.DataStorage.Reviews
{
    public class ReviewMapper : IReviewMapper
    {
        public Review ToModel(ReviewEntity entity)
        {
            return new Review
            {
                Id = entity.Id,
                MediaId = entity.MediaId,
                User = entity.User,
                Rating = entity.Rating,
                Date = entity.Date,
                Content = entity.Content
            };
        }

        public ReviewEntity ToEntity(Review model)
        {
            return new ReviewEntity
            {
                Id = model.Id,
                MediaId = model.MediaId,
                User = model.User,
                Rating = model.Rating,
                Date = model.Date,
                Content = model.Content
            };
        }
    }
}