using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;

namespace Retrospector.Search
{
    public class FactoidReducer : IFactoidReducer
    {
        public Dictionary<RetrospectorAttribute, string> Reduce(Factoid item)
        {
            return new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.FactoidTitle, item?.Title ?? ""},
                {RetrospectorAttribute.FactoidContent, item?.Content ?? ""}
            };
        }
    }
}