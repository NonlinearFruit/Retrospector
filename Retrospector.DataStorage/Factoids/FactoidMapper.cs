using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Factoids.Interfaces;

namespace Retrospector.DataStorage.Factoids
{
    public class FactoidMapper : IFactoidMapper
    {
        public Factoid ToModel(FactoidEntity entity)
        {
            return new Factoid
            {
                Id = entity.Id,
                MediaId = entity.MediaId,
                Title = entity.Title,
                Content = entity.Content
            };
        }

        public FactoidEntity ToEntity(Factoid model)
        {
            return new FactoidEntity
            {
                Id = model.Id,
                MediaId = model.MediaId,
                Title = model.Title,
                Content = model.Content
            };
        }
    }
}