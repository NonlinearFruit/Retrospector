using System;
using System.Linq;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Tests.TestDoubles;
using Retrospector.DataStorage.Tests.TestDoubles.Factoids;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Factoids
{
    public class FactoidDataGatewayTests : IDisposable
    {
        private ICrudDataGateway<Factoid> _gateway;
        private DatabaseContext_TestDouble _arrangeContext;
        private DatabaseContext_TestDouble _actContext;
        private DatabaseContext_TestDouble _assertContext;
        private FactoidMapper_TestDouble _mapper;

        public FactoidDataGatewayTests()
        {
            var id = Guid.NewGuid().ToString();
            _arrangeContext = new DatabaseContext_TestDouble(id);
            _actContext = new DatabaseContext_TestDouble(id);
            _assertContext = new DatabaseContext_TestDouble(id);
            _mapper = new FactoidMapper_TestDouble();
            _gateway = new FactoidDataGateway(_actContext, _mapper);
        }

        public class Add : FactoidDataGatewayTests
        {
            public Add()
            {
                _mapper.ReturnFor_ToEntity = new FactoidEntity();
            }

            [Fact]
            public void maps_model_to_entity()
            {
                var model = new Factoid();

                _gateway.Add(model);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(model, _mapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void stores_mapped_entity()
            {
                var entity = new FactoidEntity{Title = "American Gospel"};
                _mapper.ReturnFor_ToEntity = entity;

                _gateway.Add(null);

                Assert.Contains(entity.Title, _assertContext.Factoids.Select(f => f.Title));
            }

            [Fact]
            public void maps_entity_back_to_model()
            {
                var entity = new FactoidEntity();
                _mapper.ReturnFor_ToEntity = entity;

                _gateway.Add(null);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(entity, _mapper.LastEntityPassedTo_ToModel);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var model = new Factoid();
                _mapper.ReturnFor_ToModel = model;

                var returned = _gateway.Add(null);

                Assert.Equal(model, returned);
            }
        }

        public class Get : FactoidDataGatewayTests
        {
            [Fact]
            public void maps_entity_to_model()
            {
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.Get(entity.Id);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();
                var model = new Factoid();
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
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                Assert.ThrowsAny<Exception>(() => _gateway.Get(entity.Id+1));
            }
        }

        public class Delete : FactoidDataGatewayTests
        {
            [Fact]
            public void throws_exception_when_no_entity()
            {
                Assert.ThrowsAny<Exception>(() => _gateway.Delete(5));
            }

            [Fact]
            public void throws_exception_when_wrong_entity()
            {
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                Assert.ThrowsAny<Exception>(() => _gateway.Delete(entity.Id+1));
            }

            [Fact]
            public void removes_factoid()
            {
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "In Spirit & Truth"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.Delete(entity.Id);

                Assert.Empty(_assertContext.Factoids);
            }
        }

        public class GetAll : FactoidDataGatewayTests
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
                    _arrangeContext.Factoids.Add(new FactoidEntity());
                _arrangeContext.SaveChanges();

                _gateway.GetAll().ToList();

                Assert.Equal(numberOfEntities, _mapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void passes_entity_to_mapper()
            {
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "Genre"}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.GetAll().ToList();

                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                _arrangeContext.Factoids.Add(new FactoidEntity{Title = "Genre"});
                _arrangeContext.SaveChanges();
                var model = new Factoid();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetAll().ToList();

                Assert.Contains(model, models);
            }
        }

        public class GetByMediaId : FactoidDataGatewayTests
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
                    _arrangeContext.Factoids.Add(new FactoidEntity{MediaId = 5});
                _arrangeContext.SaveChanges();

                _gateway.GetByMediaId(mediaId).ToList();

                Assert.Equal(numberOfEntities, _mapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void passes_entity_to_mapper()
            {
                var mediaId = 5;
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "Genre", MediaId = mediaId}).Entity;
                _arrangeContext.SaveChanges();

                _gateway.GetByMediaId(mediaId).ToList();

                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                var mediaId = 5;
                _arrangeContext.Factoids.Add(new FactoidEntity{Title = "Genre", MediaId = mediaId});
                _arrangeContext.SaveChanges();
                var model = new Factoid();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetByMediaId(mediaId).ToList();

                Assert.Contains(model, models);
            }

            [Fact]
            public void does_not_return_wrong_entity()
            {
                var mediaId = 5;
                _arrangeContext.Factoids.Add(new FactoidEntity{Title = "Genre", MediaId = mediaId});
                _arrangeContext.SaveChanges();
                var model = new Factoid();
                _mapper.ReturnFor_ToModel = model;

                var models = _gateway.GetByMediaId(mediaId+1).ToList();

                Assert.Empty(models);
            }
        }

        public class Update : FactoidDataGatewayTests
        {
            public Update()
            {
                _mapper.ReturnFor_ToEntity = new FactoidEntity();
            }

            [Fact]
            public void maps_model_to_entity()
            {
                _mapper.ReturnFor_ToEntity = _arrangeContext.Factoids.Add(new FactoidEntity()).Entity;
                _arrangeContext.SaveChanges();
                var model = new Factoid();

                _gateway.Update(model);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToEntity_Calls);
                Assert.Equal(model, _mapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void maps_entity_to_model()
            {
                var entity = _arrangeContext.Factoids.Add(new FactoidEntity{Title = "Creator"}).Entity;
                _mapper.ReturnFor_ToEntity = entity;
                _arrangeContext.SaveChanges();

                _gateway.Update(null);

                Assert.Equal(Verify.Once, _mapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Title, _mapper.LastEntityPassedTo_ToModel.Title);
            }

            [Fact]
            public void returns_mapped_model()
            {
                _mapper.ReturnFor_ToEntity = _arrangeContext.Factoids.Add(new FactoidEntity()).Entity;
                _arrangeContext.SaveChanges();
                var model = new Factoid();
                _mapper.ReturnFor_ToModel = model;

                var returnedModel = _gateway.Update(null);

                Assert.Equal(model, returnedModel);
            }

            [Fact]
            public void throws_exception_when_no_entity()
            {
                _mapper.ReturnFor_ToEntity = new FactoidEntity();

                Assert.ThrowsAny<Exception>(() => _gateway.Update(null));
            }

            [Fact]
            public void throws_exception_when_no_wrong_entity()
            {
                var wrongEntity = _arrangeContext.Factoids.Add(new FactoidEntity()).Entity;
                _arrangeContext.SaveChanges();
                _mapper.ReturnFor_ToEntity = new FactoidEntity{Id = wrongEntity.Id + 1};

                Assert.ThrowsAny<Exception>(() => _gateway.Update(null));
            }

            [Fact]
            public void modifies_media_id()
            {
                var mediaId = 43;
                var entityId = _arrangeContext.Factoids.Add(new FactoidEntity()).Entity.Id;
                _arrangeContext.SaveChanges();
                _mapper.ReturnFor_ToEntity = new FactoidEntity
                {
                    Id    = entityId,
                    MediaId = mediaId
                };

                _gateway.Update(null);

                var entity = _assertContext.Factoids.First();
                Assert.Equal(mediaId, entity.MediaId);
            }

            [Fact]
            public void modifies_title()
            {
                var title = "Topic";
                var entityId = _arrangeContext.Factoids.Add(new FactoidEntity()).Entity.Id;
                _arrangeContext.SaveChanges();
                _mapper.ReturnFor_ToEntity = new FactoidEntity
                {
                    Id    = entityId,
                    Title = title
                };

                _gateway.Update(null);

                var entity = _assertContext.Factoids.First();
                Assert.Equal(title, entity.Title);
            }

            [Fact]
            public void modifies_content()
            {
                var content = "Topic";
                var entityId = _arrangeContext.Factoids.Add(new FactoidEntity()).Entity.Id;
                _arrangeContext.SaveChanges();
                _mapper.ReturnFor_ToEntity = new FactoidEntity
                {
                    Id    = entityId,
                    Content = content
                };

                _gateway.Update(null);

                var entity = _assertContext.Factoids.First();
                Assert.Equal(content, entity.Content);
            }

            [Fact]
            public void does_not_modify_created_date()
            {
                var oldDateTime = DateTime.Now.AddDays(-3);
                var entityId = _arrangeContext.Factoids.Add(new FactoidEntity{CreatedDate = oldDateTime}).Entity.Id;
                _arrangeContext.SaveChanges();
                _mapper.ReturnFor_ToEntity = new FactoidEntity
                {
                    Id    = entityId,
                    CreatedDate = DateTime.Now
                };

                _gateway.Update(null);

                var entity = _assertContext.Factoids.First();
                Assert.Equal(oldDateTime, entity.CreatedDate);
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