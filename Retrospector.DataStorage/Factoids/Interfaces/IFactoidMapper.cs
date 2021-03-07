using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Factoids.Entities;

namespace Retrospector.DataStorage.Factoids.Interfaces
{
    public interface IFactoidMapper
    {
        Factoid ToModel(FactoidEntity entity);
        FactoidEntity ToEntity(Factoid model);
    }
}