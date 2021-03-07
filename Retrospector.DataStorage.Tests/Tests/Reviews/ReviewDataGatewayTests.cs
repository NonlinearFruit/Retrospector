using System;
using System.Linq;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Reviews;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Tests.TestDoubles;
using Retrospector.DataStorage.Tests.TestDoubles.Reviews;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Reviews
{
    public class ReviewDataGatewayTests : IDisposable
    {
        private ICrudDataGateway<Review> _gateway;
        private DatabaseContext_TestDouble _arrangeContext;
        private DatabaseContext_TestDouble _actContext;
        private DatabaseContext_TestDouble _assertContext;
        private ReviewMapper_TestDouble _mapper;

        public ReviewDataGatewayTests()
        {
            var id = Guid.NewGuid().ToString();
            _arrangeContext = new DatabaseContext_TestDouble(id);
            _actContext = new DatabaseContext_TestDouble(id);
            _assertContext = new DatabaseContext_TestDouble(id);
            _mapper = new ReviewMapper_TestDouble();
            _gateway = new ReviewDataGateway(_actContext, _mapper);
        }

        public class Add : ReviewDataGatewayTests
        {
            public Add()
            {
                _mapper.ReturnFor_ToEntity = new ReviewEntity();
            }

            [Fact]
            public void maps_model_to_entity()
            {
                var model = new Review();

                _gateway.Add(model);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(model, _mapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void stores_mapped_entity()
            {
                var entity = new ReviewEntity{Content = "American Gospel"};
                _mapper.ReturnFor_ToEntity = entity;

                _gateway.Add(null);

                Assert.Contains(entity.Content, _assertContext.Reviews.Select(f => f.Content));
            }

            [Fact]
            public void maps_entity_back_to_model()
            {
                var entity = new ReviewEntity();
                _mapper.ReturnFor_ToEntity = entity;

                _gateway.Add(null);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(entity, _mapper.LastEntityPassedTo_ToModel);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var model = new Review();
                _mapper.ReturnFor_ToModel = model;

                var returned = _gateway.Add(null);

                Assert.Equal(model, returned);
            }
        }

        public class Get : ReviewDataGatewayTests
        {
            [Fact]
            public void maps_entity_to_model()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.Get(entity.Id);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Content, _mapper.LastEntityPassedTo_ToModel.Content);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();
                var model = new Review();
                _mapper.ReturnFor_ToModel = model;

                var returnedModel = _gateway.Get(entity.Id);

                Assert.Equal(model, returnedModel);
            }

            [Fact]
            public void throws_exception_when_no_entity()
            {
                Assert.ThrowsAny<Exception>(() => _gateway.Get(0));
            }

            [Fact]
            public void throws_exception_when_wrong_entity()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                Assert.ThrowsAny<Exception>(() => _gateway.Get(entity.Id+1));
            }
        }

        public class Delete : ReviewDataGatewayTests
        {
            [Fact]
            public void throws_exception_when_no_entity()
            {
                Assert.ThrowsAny<Exception>(() => _gateway.Delete(5));
            }

            [Fact]
            public void throws_exception_when_wrong_entity()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                Assert.ThrowsAny<Exception>(() => _gateway.Delete(entity.Id+1));
            }

            [Fact]
            public void removes_factoid()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.Delete(entity.Id);

                Assert.Empty(_assertContext.Reviews);
            }
        }

        public class GetAll : ReviewDataGatewayTests
        {
            [Fact]
            public void empty_list_when_no_entities()
            {
                var entities = _gateway.GetAll();

                Assert.Empty(entities);
            }

            [Theory]
            [InlineData(0)]
            [InlineData(1)]
            [InlineData(10)]
            public void calls_mapper(int numberOfEntities)
            {
                for (var i = 0; i < numberOfEntities; i++)
                    _arrangeContext.Reviews.Add(new ReviewEntity());
                _arrangeContext.SaveChanges();

                _gateway.GetAll().ToList();

                Assert.Equal(numberOfEntities, _mapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void passes_entity_to_mapper()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "Genre"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.GetAll().ToList();

                Assert.Equal(entity.Content, _mapper.LastEntityPassedTo_ToModel.Content);
            }

            [Fact]
            public void returns_mapped_model()
            {
                _arrangeContext.Reviews.Add(new ReviewEntity{Content = "Genre"});
                _arrangeContext.SaveChanges();
                var model = new Review();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetAll().ToList();

                Assert.Contains(model, models);
            }
        }

        public class GetByMediaId : ReviewDataGatewayTests
        {
            [Fact]
            public void empty_list_when_no_entities()
            {
                var entities = _gateway.GetByMediaId(0);

                Assert.Empty(entities);
            }

            [Theory]
            [InlineData(0)]
            [InlineData(1)]
            [InlineData(10)]
            public void calls_mapper(int numberOfEntities)
            {
                var mediaId = 5;
                for (var i = 0; i < numberOfEntities; i++)
                    _arrangeContext.Reviews.Add(new ReviewEntity{MediaId = 5});
                _arrangeContext.SaveChanges();

                _gateway.GetByMediaId(mediaId).ToList();

                Assert.Equal(numberOfEntities, _mapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void passes_entity_to_mapper()
            {
                var mediaId = 5;
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "Genre", MediaId = mediaId}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.GetByMediaId(mediaId).ToList();

                Assert.Equal(entity.Content, _mapper.LastEntityPassedTo_ToModel.Content);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var mediaId = 5;
                _arrangeContext.Reviews.Add(new ReviewEntity{Content = "Genre", MediaId = mediaId});
                _arrangeContext.SaveChanges();
                var model = new Review();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetByMediaId(mediaId).ToList();

                Assert.Contains(model, models);
            }

            [Fact]
            public void does_not_return_wrong_entity()
            {
                var mediaId = 5;
                _arrangeContext.Reviews.Add(new ReviewEntity{Content = "Genre", MediaId = mediaId});
                _arrangeContext.SaveChanges();
                var model = new Review();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetByMediaId(mediaId+1).ToList();

                Assert.Empty(models);
            }
        }

        public class Update : ReviewDataGatewayTests
        {
            public Update()
            {
                _mapper.ReturnFor_ToEntity = new ReviewEntity();
            }

            [Fact]
            public void maps_model_to_entity()
            {
                _mapper.ReturnFor_ToEntity = _arrangeContext.Reviews.Add(new ReviewEntity()).Entity;
                _arrangeContext.SaveChanges();
                var model = new Review();

                _gateway.Update(model);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(model, _mapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void maps_entity_to_model()
            {
                var entity = _arrangeContext.Reviews.Add(new ReviewEntity{Content = "Creator"}).Entity;
                _mapper.ReturnFor_ToEntity = entity;
                _arrangeContext.SaveChanges();

                _gateway.Update(null);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Content, _mapper.LastEntityPassedTo_ToModel.Content);
            }

            [Fact]
            public void returns_mapped_model()
            {
                _mapper.ReturnFor_ToEntity = _arrangeContext.Reviews.Add(new ReviewEntity()).Entity;
                _arrangeContext.SaveChanges();
                var model = new Review();
                _mapper.ReturnFor_ToModel = model;

                var returnedModel = _gateway.Update(null);

                Assert.Equal(model, returnedModel);
            }

            [Fact]
            public void throws_exception_when_no_entity()
            {
                _mapper.ReturnFor_ToEntity = new ReviewEntity();

                Assert.ThrowsAny<Exception>(() => _gateway.Update(null));
            }

            [Fact]
            public void throws_exception_when_no_wrong_entity()
            {
                var wrongEntityId = ArrangeReviewEntity();
                _mapper.ReturnFor_ToEntity = new ReviewEntity{Id = wrongEntityId + 1};

                Assert.ThrowsAny<Exception>(() => _gateway.Update(null));
            }

            [Fact]
            public void modifies_media_id()
            {
                var mediaId = 43;
                var entityId = ArrangeReviewEntity();
                _mapper.ReturnFor_ToEntity = new ReviewEntity
                {
                    Id    = entityId,
                    MediaId = mediaId
                };

                _gateway.Update(null);

                var entity = _assertContext.Reviews.First();
                Assert.Equal(mediaId, entity.MediaId);
            }

            [Fact]
            public void modifies_rating()
            {
                var rating = 5;
                var entityId = ArrangeReviewEntity();
                _mapper.ReturnFor_ToEntity = new ReviewEntity
                {
                    Id = entityId,
                    Rating = rating
                };

                _gateway.Update(null);

                var entity = _assertContext.Reviews.First();
                Assert.Equal(rating, entity.Rating);
            }

            [Fact]
            public void modifies_date()
            {
                var date = DateTime.Now;
                var entityId = ArrangeReviewEntity();
                _mapper.ReturnFor_ToEntity = new ReviewEntity
                {
                    Id = entityId,
                    Date = date
                };

                _gateway.Update(null);

                var entity = _assertContext.Reviews.First();
                Assert.Equal(date, entity.Date);
            }

            [Fact]
            public void modifies_user()
            {
                var user = "Jon";
                var entityId = ArrangeReviewEntity();
                _mapper.ReturnFor_ToEntity = new ReviewEntity
                {
                    Id = entityId,
                    User = user
                };

                _gateway.Update(null);

                var entity = _assertContext.Reviews.First();
                Assert.Equal(user, entity.User);
            }

            [Fact]
            public void modifies_content()
            {
                var content = "Topic";
                var entityId = ArrangeReviewEntity();
                _mapper.ReturnFor_ToEntity = new ReviewEntity
                {
                    Id    = entityId,
                    Content = content
                };

                _gateway.Update(null);

                var entity = _assertContext.Reviews.First();
                Assert.Equal(content, entity.Content);
            }

            [Fact]
            public void does_not_modify_created_date()
            {
                var oldDateTime = DateTime.Now.AddDays(-3);
                var entityId = ArrangeReviewEntity(new ReviewEntity {CreatedDate = oldDateTime});
                _mapper.ReturnFor_ToEntity = new ReviewEntity
                {
                    Id    = entityId,
                    CreatedDate = DateTime.Now
                };

                _gateway.Update(null);

                var entity = _assertContext.Reviews.First();
                Assert.Equal(oldDateTime, entity.CreatedDate);
            }

            private int ArrangeReviewEntity(ReviewEntity entity = null)
            {
                entity ??= new ReviewEntity();
                var entityId = _arrangeContext.Reviews.Add(entity).Entity.Id;
                _arrangeContext.SaveChanges();
                return entityId;
            }
        }

        public void Dispose()
        {
            _arrangeContext?.Dispose();
            _actContext?.Dispose();
            _assertContext?.Dispose();
        }
    }
}