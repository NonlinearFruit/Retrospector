using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Medias;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Medias.Interfaces;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Medias
{
    public class MediaTypeMapperTests
    {
        private IMediaTypeMapper _mapper;

        public MediaTypeMapperTests()
        {
            _mapper = new MediaTypeMapper();
        }

        public class ToModel : MediaTypeMapperTests
        {
            [Theory]
            [InlineData(MediaType.Series, MediaTypeEntity.Series)]
            [InlineData(MediaType.MiniSeries, MediaTypeEntity.MiniSeries)]
            [InlineData(MediaType.Single, MediaTypeEntity.Single)]
            [InlineData(MediaType.Wishlist, MediaTypeEntity.Wishlist)]
            public void maps(MediaType model, MediaTypeEntity entity)
            {
                Assert.Equal(model, _mapper.ToModel(entity));
            }
        }

        public class ToEntity : MediaTypeMapperTests
        {
            [Theory]
            [InlineData(MediaType.Series, MediaTypeEntity.Series)]
            [InlineData(MediaType.MiniSeries, MediaTypeEntity.MiniSeries)]
            [InlineData(MediaType.Single, MediaTypeEntity.Single)]
            [InlineData(MediaType.Wishlist, MediaTypeEntity.Wishlist)]
            public void maps(MediaType model, MediaTypeEntity entity)
            {
                Assert.Equal(entity, _mapper.ToEntity(model));
            }
        }
    }
}