using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Factoids.Interfaces;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Factoids
{
    public class FactoidMapperTests
    {
        private IFactoidMapper _mapper;

        public FactoidMapperTests()
        {
            _mapper = new FactoidMapper();
        }

        public class ToModel : FactoidMapperTests
        {
            [Fact]
            public void maps_properties()
            {
                var entity = new FactoidEntity
                {
                    Id = 4,
                    MediaId = 7,
                    Title = "Genre",
                    Content = "Comedy"
                };

                var model = _mapper.ToModel(entity);

                Assert.Equal(entity.Id, model.Id);
                Assert.Equal(entity.MediaId, model.MediaId);
                Assert.Equal(entity.Title, model.Title);
                Assert.Equal(entity.Content, model.Content);
            }
        }

        public class ToEntity : FactoidMapperTests
        {
            [Fact]
            public void maps_properties()
            {
                var model = new Factoid
                {
                    Id = 4,
                    MediaId = 7,
                    Title = "Genre",
                    Content = "Comedy"
                };

                var entity = _mapper.ToEntity(model);

                Assert.Equal(model.Id, entity.Id);
                Assert.Equal(model.MediaId, entity.MediaId);
                Assert.Equal(model.Title, entity.Title);
                Assert.Equal(model.Content, entity.Content);
            }
        }
    }
}