using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Tests.TestDoubles.Search
{
    public class QueryBuilder_TestDouble : IQueryBuilder
    {
        public QueryTree ReturnFor_BuildQuery { get; set; }
        public string LastQueryPassedTo_BuildQuery { get; set; }
        public int CountOf_BuildQuery_Calls { get; set; }
        public QueryTree BuildQuery(string query)
        {
            CountOf_BuildQuery_Calls++;
            LastQueryPassedTo_BuildQuery = query;
            return ReturnFor_BuildQuery;
        }
    }
}