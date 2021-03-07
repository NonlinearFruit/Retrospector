using System.Collections.Generic;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search.Interfaces
{
    public interface ISearchDataGateway
    {
        IEnumerable<Dictionary<RetrospectorAttribute, string>> Search(QueryTree query);
    }
}