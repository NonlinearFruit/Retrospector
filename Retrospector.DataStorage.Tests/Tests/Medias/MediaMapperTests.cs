using System.Linq;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Medias.Interfaces;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Tests.TestDoubles.Factoids;
using Retrospector.DataStorage.Tests.TestDoubles.Medias;
using Retrospector.DataStorage.Tests.TestDoubles.Reviews;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Medias
{
    public class MediaMapperTests
    {
        private IMediaMapper _mapper;
        private MediaTypeMapper_TestDouble _typeMapper;
        private FactoidMapper_TestDouble _factoidMapper;
        private ReviewMapper_TestDouble _reviewMapper;

        public MediaMapperTests()
        {
            _factoidMapper = new FactoidMapper_TestDouble();
            _reviewMapper = new ReviewMapper_TestDouble();
            _typeMapper = new MediaTypeMapper_TestDouble();
            _mapper = new MediaMapper(_reviewMapper, _factoidMapper, _typeMapper);
        }

        public class ToModel : MediaMapperTests
        {
            [Fact]
            public void maps_properties()
            {
                var entity = new MediaEntity
                {
                    Id = 4,
                    Title = "Hound of the Baskervilles",
                    Creator = "AC Doyle",
                    SeasonId = "S1",
                    EpisodeId = "E1",
                    Category = "Book",
                    Description = "a book"
                };

                var model = _mapper.ToModel(entity);

                Assert.Equal(entity.Id, model.Id);
                Assert.Equal(entity.Title, model.Title);
                Assert.Equal(entity.Creator, model.Creator);
                Assert.Equal(entity.SeasonId, model.SeasonId);
                Assert.Equal(entity.EpisodeId, model.EpisodeId);
                Assert.Equal(entity.Category, model.Category);
                Assert.Equal(entity.Description, model.Description);
            }

            [Fact]
            public void maps_type()
            {
                var entity = new MediaEntity
                {
                    Type = MediaTypeEntity.Series
                };

                _mapper.ToModel(entity);

                Assert.Equal(Verify.Once, _typeMapper.CountOf_ToModel_Calls);
                Assert.Equal(entity.Type, _typeMapper.LastEntityPassedTo_ToModel);
            }

            [Fact]
            public void returns_mapped_type()
            {
                var type = MediaType.Series;
                var entity = new MediaEntity();
                _typeMapper.ReturnFor_ToModel = type;

                var model = _mapper.ToModel(entity);

                Assert.Equal(type, model.Type);
            }

            [Fact]
            public void maps_review()
            {
                var review = new ReviewEntity();
                var entity = new MediaEntity
                {
                    Reviews = new []{review}
                };

                _mapper.ToModel(entity);

                Assert.Equal(Verify.Once, _reviewMapper.CountOf_ToModel_Calls);
                Assert.Equal(review, _reviewMapper.LastEntityPassedTo_ToModel);
            }

            [Theory]
            [InlineData(0)]
            [InlineData(1)]
            [InlineData(10)]
            public void maps_multiple_reviews(int numberOfReviews)
            {
                var entity = new MediaEntity
                {
                    Reviews = Enumerable.Repeat(new ReviewEntity(), numberOfReviews).ToList()
                };

                _mapper.ToModel(entity);

                Assert.Equal(numberOfReviews, _reviewMapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void returns_mapped_reviews()
            {
                var expectedReview = new Review();
                var entity = new MediaEntity
                {
                    Reviews = new [] {new ReviewEntity()}
                };
                _reviewMapper.ReturnFor_ToModel = expectedReview;

                var model = _mapper.ToModel(entity);

                var actualReview = Assert.Single(model.Reviews);
                Assert.Equal(expectedReview, actualReview);
            }

            [Fact]
            public void maps_factoid()
            {
                var factoid = new FactoidEntity();
                var entity = new MediaEntity
                {
                    Factoids = new []{factoid}
                };

                _mapper.ToModel(entity);

                Assert.Equal(Verify.Once, _factoidMapper.CountOf_ToModel_Calls);
                Assert.Equal(factoid, _factoidMapper.LastEntityPassedTo_ToModel);
            }

            [Theory]
            [InlineData(0)]
            [InlineData(1)]
            [InlineData(10)]
            public void maps_multiple_factoids(int numberOfFactoids)
            {
                var entity = new MediaEntity
                {
                    Factoids = Enumerable.Repeat(new FactoidEntity(), numberOfFactoids).ToList()
                };

                _mapper.ToModel(entity);

                Assert.Equal(numberOfFactoids, _factoidMapper.CountOf_ToModel_Calls);
            }

            [Fact]
            public void returns_mapped_factoids()
            {
                var expectedFactoid = new Factoid();
                var entity = new MediaEntity
                {
                    Factoids = new [] {new FactoidEntity()}
                };
                _factoidMapper.ReturnFor_ToModel = expectedFactoid;

                var model = _mapper.ToModel(entity);

                var actualFactoid = Assert.Single(model.Factoids);
                Assert.Equal(expectedFactoid, actualFactoid);
            }
        }

        public class ToEntity : MediaMapperTests
        {
            [Fact]
            public void maps_properties()
            {
                var model = new Media
                {
                    Id = 4,
                    Title = "Hound of the Baskervilles",
                    Creator = "AC Doyle",
                    SeasonId = "S1",
                    EpisodeId = "E1",
                    Category = "Book",
                    Description = "a book"
                };

                var entity = _mapper.ToEntity(model);

                Assert.Equal(model.Id, entity.Id);
                Assert.Equal(model.Title, entity.Title);
                Assert.Equal(entity.Creator, model.Creator);
                Assert.Equal(model.SeasonId, entity.SeasonId);
                Assert.Equal(model.EpisodeId, entity.EpisodeId);
                Assert.Equal(model.Category, entity.Category);
                Assert.Equal(model.Description, entity.Description);
            }

            [Fact]
            public void maps_type()
            {
                var model = new Media
                {
                    Type = MediaType.Series
                };

                _mapper.ToEntity(model);

                Assert.Equal(Verify.Once, _typeMapper.CountOf_ToEntity_Calls);
                Assert.Equal(model.Type, _typeMapper.LastModelPassedTo_ToEntity);
            }

            [Fact]
            public void returns_mapped_type()
            {
                var type = MediaTypeEntity.Series;
                var model = new Media();
                _typeMapper.ReturnFor_ToEntity = type;

                var entity = _mapper.ToEntity(model);

                Assert.Equal(type, entity.Type);
            }
        }
    }
}