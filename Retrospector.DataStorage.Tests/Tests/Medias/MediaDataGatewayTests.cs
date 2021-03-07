using System;
using System.Linq;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Medias;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Tests.TestDoubles;
using Retrospector.DataStorage.Tests.TestDoubles.Medias;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Medias
{
    public class MediaDataGatewayTests : IDisposable
    {
        private ICrudDataGateway<Media> _gateway;
        private DatabaseContext_TestDouble _arrangeContext;
        private DatabaseContext_TestDouble _actContext;
        private DatabaseContext_TestDouble _assertContext;
        private MediaMapper_TestDouble _mapper;

        public MediaDataGatewayTests()
        {
            var id = Guid.NewGuid().ToString();
            _arrangeContext = new DatabaseContext_TestDouble(id);
            _actContext = new DatabaseContext_TestDouble(id);
            _assertContext = new DatabaseContext_TestDouble(id);
            _mapper = new MediaMapper_TestDouble();
            _gateway = new MediaDataGateway(_actContext, _mapper);
        }

        public class Add : MediaDataGatewayTests
        {
            public Add()
            {
                _mapper.ReturnFor_ToEntity = new MediaEntity();
            }

            [Fact]
            public void maps_model_to_entity()
            {
                var model = new Media();

                _gateway.Add(model);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(model, _mapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void stores_mapped_entity()
            {
                var entity = new MediaEntity{Title = "American Gospel"};
                _mapper.ReturnFor_ToEntity = entity;

                _gateway.Add(null);

                Assert.Contains(entity.Title, _assertContext.Media.Select(f => f.Title));
            }

            [Fact]
            public void maps_entity_back_to_model()
            {
                var entity = new MediaEntity();
                _mapper.ReturnFor_ToEntity = entity;

                _gateway.Add(null);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(entity, _mapper.LastEntityPassedTo_ToModel);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var model = new Media();
                _mapper.ReturnFor_ToModel = model;

                var returned = _gateway.Add(null);

                Assert.Equal(model, returned);
            }
        }

        public class Get : MediaDataGatewayTests
        {
            [Fact]
            public void maps_entity_to_model()
            {
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.Get(entity.Id);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();
                var model = new Media();
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
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                Assert.ThrowsAny<Exception>(() => _gateway.Get(entity.Id+1));
            }
        }

        public class Delete : MediaDataGatewayTests
        {
            [Fact]
            public void throws_exception_when_no_entity()
            {
                Assert.ThrowsAny<Exception>(() => _gateway.Delete(5));
            }

            [Fact]
            public void throws_exception_when_wrong_entity()
            {
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                Assert.ThrowsAny<Exception>(() => _gateway.Delete(entity.Id+1));
            }

            [Fact]
            public void removes_factoid()
            {
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.Delete(entity.Id);

                Assert.Empty(_assertContext.Media);
            }
        }

        public class GetAll : MediaDataGatewayTests
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
                    _arrangeContext.Media.Add(new MediaEntity());
                _arrangeContext.SaveChanges();

                _gateway.GetAll().ToList();

                Assert.Equal(numberOfEntities, _mapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void passes_entity_to_mapper()
            {
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "Genre"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.GetAll().ToList();

                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                _arrangeContext.Media.Add(new MediaEntity{Title = "Genre"});
                _arrangeContext.SaveChanges();
                var model = new Media();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetAll().ToList();

                Assert.Contains(model, models);
            }
        }

        public class GetByMediaId : MediaDataGatewayTests
        {
            [Fact]
            public void empty_list_when_no_entities()
            {
                var entities = _gateway.GetByMediaId(0);

                Assert.Empty(entities);
            }

            [Fact]
            public void calls_mapper()
            {
                var mediaId = 5;
                _arrangeContext.Media.Add(new MediaEntity{Id = mediaId});
                _arrangeContext.SaveChanges();

                _gateway.GetByMediaId(mediaId).ToList();

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void passes_entity_to_mapper()
            {
                var mediaId = 5;
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "Genre", Id = mediaId}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.GetByMediaId(mediaId).ToList();

                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var mediaId = 5;
                _arrangeContext.Media.Add(new MediaEntity{Title = "Genre", Id = mediaId});
                _arrangeContext.SaveChanges();
                var model = new Media();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetByMediaId(mediaId).ToList();

                Assert.Contains(model, models);
            }

            [Fact]
            public void does_not_return_wrong_entity()
            {
                var mediaId = 5;
                _arrangeContext.Media.Add(new MediaEntity{Title = "Genre", Id = mediaId});
                _arrangeContext.SaveChanges();
                var model = new Media();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetByMediaId(mediaId+1).ToList();

                Assert.Empty(models);
            }
        }

        public class Update : MediaDataGatewayTests
        {
            public Update()
            {
                _mapper.ReturnFor_ToEntity = new MediaEntity();
            }

            [Fact]
            public void maps_model_to_entity()
            {
                _mapper.ReturnFor_ToEntity = _arrangeContext.Media.Add(new MediaEntity()).Entity;
                _arrangeContext.SaveChanges();
                var model = new Media();

                _gateway.Update(model);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(model, _mapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void maps_entity_to_model()
            {
                var entity = _arrangeContext.Media.Add(new MediaEntity{Title = "Creator"}).Entity;
                _mapper.ReturnFor_ToEntity = entity;
                _arrangeContext.SaveChanges();

                _gateway.Update(null);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                _mapper.ReturnFor_ToEntity = _arrangeContext.Media.Add(new MediaEntity()).Entity;
                _arrangeContext.SaveChanges();
                var model = new Media();
                _mapper.ReturnFor_ToModel = model;

                var returnedModel = _gateway.Update(null);

                Assert.Equal(model, returnedModel);
            }

            [Fact]
            public void throws_exception_when_no_entity()
            {
                _mapper.ReturnFor_ToEntity = new MediaEntity();

                Assert.ThrowsAny<Exception>(() => _gateway.Update(null));
            }

            [Fact]
            public void throws_exception_when_no_wrong_entity()
            {
                var wrongEntity = _arrangeContext.Media.Add(new MediaEntity()).Entity;
                _arrangeContext.SaveChanges();
                _mapper.ReturnFor_ToEntity = new MediaEntity{Id = wrongEntity.Id + 1};

                Assert.ThrowsAny<Exception>(() => _gateway.Update(null));
            }

            [Theory]
            [InlineData(nameof(MediaEntity.Title), "Sherlock Holmes")]
            [InlineData(nameof(MediaEntity.Creator), "AC Doyle")]
            [InlineData(nameof(MediaEntity.SeasonId), "S1")]
            [InlineData(nameof(MediaEntity.EpisodeId), "E1")]
            [InlineData(nameof(MediaEntity.Category), "Book")]
            [InlineData(nameof(MediaEntity.Description), "a book")]
            [InlineData(nameof(MediaEntity.Type), MediaTypeEntity.Series)]
            public void modifies_property(string property, dynamic value)
            {
                var entityId = ArrangeMediaEntity();
                _mapper.ReturnFor_ToEntity.Id = entityId;
                Reflection.SetProperty(_mapper.ReturnFor_ToEntity, property, value);

                _gateway.Update(null);

                var entity = _assertContext.Media.First();
                VerifyProperty(entity, property, value);
            }

            [Fact]
            public void does_not_modify_created_date()
            {
                var oldDateTime = DateTime.Now.AddDays(-3);
                var property = nameof(MediaEntity.CreatedDate);
                var entityId = ArrangeMediaEntity(new MediaEntity{CreatedDate = oldDateTime});
                _mapper.ReturnFor_ToEntity.Id = entityId;
                Reflection.SetProperty(_mapper.ReturnFor_ToEntity, property, DateTime.Now);

                _gateway.Update(null);

                var entity = _assertContext.Media.First();
                VerifyProperty(entity, property, oldDateTime);
            }

            private void VerifyProperty<T>(object obj, string property, T expected)
            {
                var actual = Reflection.GetProperty<T>(obj, property);
                Assert.Equal(expected, actual);
            }

            private int ArrangeMediaEntity(MediaEntity entity = null)
            {
                entity ??= new MediaEntity();
                var entityId = _arrangeContext.Media.Add(entity).Entity.Id;
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