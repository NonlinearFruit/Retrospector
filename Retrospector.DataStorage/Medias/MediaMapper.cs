using System.Linq;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids.Interfaces;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Medias.Interfaces;
using Retrospector.DataStorage.Reviews.Interfaces;

namespace Retrospector.DataStorage.Medias
{
    public class MediaMapper : IMediaMapper
    {
        private readonly IReviewMapper _reviewMapper;
        private readonly IFactoidMapper _factoidMapper;
        private readonly IMediaTypeMapper _typeMapper;

        public MediaMapper(IReviewMapper reviewMapper, IFactoidMapper factoidMapper, IMediaTypeMapper typeMapper)
        {
            _reviewMapper = reviewMapper;
            _factoidMapper = factoidMapper;
            _typeMapper = typeMapper;
        }

        public Media ToModel(MediaEntity entity)
        {
            return new Media
            {
                Id = entity.Id,
                Title = entity.Title,
                Creator = entity.Creator,
                SeasonId = entity.SeasonId,
                EpisodeId = entity.EpisodeId,
                Category = entity.Category,
                Description = entity.Description,
                Type = _typeMapper.ToModel(entity.Type),
                Reviews = entity.Reviews.Select(_reviewMapper.ToModel).ToList(),
                Factoids = entity.Factoids.Select(_factoidMapper.ToModel).ToList()
            };
        }

        public MediaEntity ToEntity(Media model)
        {
            return new MediaEntity
            {
                Id = model.Id,
                Title = model.Title,
                Creator = model.Creator,
                SeasonId = model.SeasonId,
                EpisodeId = model.EpisodeId,
                Category = model.Category,
                Description = model.Description,
                Type = _typeMapper.ToEntity(model.Type)
            };
        }
    }
}