using System;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Reviews;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Reviews.Interfaces;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Reviews
{
    public class ReviewMapperTests
    {
        private IReviewMapper _mapper;

        public ReviewMapperTests()
        {
            _mapper = new ReviewMapper();
        }

        public class ToModel : ReviewMapperTests
        {
            [Fact]
            public void maps_properties()
            {
                var entity = new ReviewEntity
                {
                    Id = 4,
                    MediaId = 7,
                    User = "Ben",
                    Rating = 10,
                    Date = DateTime.Now,
                    Content = "My Review of Media #7"
                };

                var model = _mapper.ToModel(entity);

                Assert.Equal(entity.Id, model.Id);
                Assert.Equal(entity.MediaId, model.MediaId);
                Assert.Equal(entity.User, model.User);
                Assert.Equal(entity.Rating, model.Rating);
                Assert.Equal(entity.Date, model.Date);
                Assert.Equal(entity.Content, model.Content);
            }
        }

        public class ToEntity : ReviewMapperTests
        {
            [Fact]
            public void maps_properties()
            {
                var model = new Review
                {
                    Id = 4,
                    MediaId = 7,
                    User = "Ben",
                    Rating = 10,
                    Date = DateTime.Now,
                    Content = "My Review of Media #7"
                };

                var entity = _mapper.ToEntity(model);

                Assert.Equal(model.Id, entity.Id);
                Assert.Equal(model.MediaId, entity.MediaId);
                Assert.Equal(model.User, entity.User);
                Assert.Equal(model.Rating, entity.Rating);
                Assert.Equal(model.Date, entity.Date);
                Assert.Equal(model.Content, entity.Content);
            }
        }
    }
}