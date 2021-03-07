using System.Collections.Generic;
using System.Linq;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Medias.Interfaces;

namespace Retrospector.DataStorage.Medias
{
    public class MediaDataGateway : ICrudDataGateway<Media>
    {
        private readonly IDatabaseContext _context;
        private readonly IMediaMapper _mapper;

        public MediaDataGateway(IDatabaseContext context, IMediaMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public Media Add(Media model)
        {
            var entity = _mapper.ToEntity(model);
            var newEntity = _context.Media.Add(entity).Entity;
            var newModel = _mapper.ToModel(newEntity);
            _context.SaveChanges();
            return newModel;
        }

        public Media Get(int id)
        {
            return _mapper.ToModel(_context.Media.First(f => f.Id == id));
        }

        public Media Update(Media model)
        {
            var entity = _mapper.ToEntity(model);
            var storedEntity = _context.Media.First(f => f.Id == entity.Id);
            storedEntity.Title = entity.Title;
            storedEntity.Creator = entity.Creator;
            storedEntity.SeasonId = entity.SeasonId;
            storedEntity.EpisodeId = entity.EpisodeId;
            storedEntity.Category = entity.Category;
            storedEntity.Description = entity.Description;
            storedEntity.Type = entity.Type;
            _context.SaveChanges();
            return _mapper.ToModel(storedEntity);
        }

        public void Delete(int id)
        {
            var factoid = _context.Media.Attach(new MediaEntity {Id = id}).Entity;
            _context.Media.Remove(factoid);
            _context.SaveChanges();
        }

        public IEnumerable<Media> GetAll()
        {
            return _context
                .Media
                .Select(_mapper.ToModel)
                .AsEnumerable();
        }

        public IEnumerable<Media> GetByMediaId(int mediaId)
        {
            return _context
                .Media
                .Where(entity => entity.Id == mediaId)
                .AsEnumerable()
                .Select(_mapper.ToModel);
        }
    }
}