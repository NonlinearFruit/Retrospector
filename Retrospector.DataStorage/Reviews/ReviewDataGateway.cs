using System.Collections.Generic;
using System.Linq;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Reviews.Interfaces;

namespace Retrospector.DataStorage.Reviews
{
    public class ReviewDataGateway : ICrudDataGateway<Review>
    {
        private readonly IDatabaseContext _context;
        private readonly IReviewMapper _mapper;

        public ReviewDataGateway(IDatabaseContext context, IReviewMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public Review Add(Review model)
        {
            var entity = _mapper.ToEntity(model);
            var newEntity = _context.Reviews.Add(entity).Entity;
            var newModel = _mapper.ToModel(newEntity);
            _context.SaveChanges();
            return newModel;
        }

        public Review Get(int id)
        {
            return _mapper.ToModel(_context.Reviews.First(f => f.Id == id));
        }

        public Review Update(Review model)
        {
            var entity = _mapper.ToEntity(model);
            var storedEntity = _context.Reviews.First(f => f.Id == entity.Id);
            storedEntity.MediaId = entity.MediaId;
            storedEntity.User = entity.User;
            storedEntity.Date = entity.Date;
            storedEntity.Rating = entity.Rating;
            storedEntity.Content = entity.Content;
            _context.SaveChanges();
            return _mapper.ToModel(storedEntity);
        }

        public void Delete(int id)
        {
            var factoid = _context.Reviews.Attach(new ReviewEntity {Id = id}).Entity;
            _context.Reviews.Remove(factoid);
            _context.SaveChanges();
        }

        public IEnumerable<Review> GetAll()
        {
            return _context
                .Reviews
                .Select(_mapper.ToModel)
                .AsEnumerable();
        }

        public IEnumerable<Review> GetByMediaId(int mediaId)
        {
            return _context
                .Reviews
                .Where(entity => entity.MediaId == mediaId)
                .AsEnumerable()
                .Select(_mapper.ToModel);
        }
    }
}