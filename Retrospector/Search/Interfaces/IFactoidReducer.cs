using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage;

namespace Retrospector.Search.Interfaces
{
    public interface IFactoidReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(FactoidEntity item);
    }
}