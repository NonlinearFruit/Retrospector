using System.Collections.Generic;
using System.Linq;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Factoids.Interfaces;

namespace Retrospector.DataStorage.Factoids
{
    public class FactoidDataGateway : ICrudDataGateway<Factoid>
    {
        private readonly IDatabaseContext _context;
        private readonly IFactoidMapper _mapper;

        public FactoidDataGateway(IDatabaseContext context, IFactoidMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public Factoid Add(Factoid model)
        {
            var entity = _mapper.ToEntity(model);
            var newEntity = _context.Factoids.Add(entity).Entity;
            var newModel = _mapper.ToModel(newEntity);
            _context.SaveChanges();
            return newModel;
        }

        public Factoid Get(int id)
        {
            return _mapper.ToModel(_context.Factoids.First(f => f.Id == id));
        }

        public Factoid Update(Factoid model)
        {
            var entity = _mapper.ToEntity(model);
            var storedEntity = _context.Factoids.First(f => f.Id == entity.Id);
            storedEntity.MediaId = entity.MediaId;
            storedEntity.Title = entity.Title;
            storedEntity.Content = entity.Content;
            _context.SaveChanges();
            return _mapper.ToModel(storedEntity);
        }

        public void Delete(int id)
        {
            var factoid = _context.Factoids.Attach(new FactoidEntity {Id = id}).Entity;
            _context.Factoids.Remove(factoid);
            _context.SaveChanges();
        }

        public IEnumerable<Factoid> GetAll()
        {
            return _context
                .Factoids
                .Select(_mapper.ToModel)
                .AsEnumerable();
        }

        public IEnumerable<Factoid> GetByMediaId(int mediaId)
        {
            return _context
                .Factoids
                .Where(entity => entity.MediaId == mediaId)
                .AsEnumerable()
                .Select(_mapper.ToModel);
        }
    }
}