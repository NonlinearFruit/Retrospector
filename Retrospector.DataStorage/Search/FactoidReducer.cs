using System.Collections.Generic;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Search.Interfaces;

namespace Retrospector.DataStorage.Search
{
    public class FactoidReducer : IFactoidReducer
    {
        public Dictionary<RetrospectorAttribute, string> Reduce(FactoidEntity item)
        {
            return new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.FactoidTitle, item?.Title ?? ""},
                {RetrospectorAttribute.FactoidContent, item?.Content ?? ""}
            };
        }
    }
}