using System.Collections.Generic;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;

namespace Retrospector.DataStorage.Search.Interfaces
{
    public interface IFactoidReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(FactoidEntity item);
    }
}