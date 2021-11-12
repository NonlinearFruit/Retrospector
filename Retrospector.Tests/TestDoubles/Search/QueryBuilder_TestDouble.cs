using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Tests.TestDoubles.Search
{
    public class QueryBuilder_TestDouble : IQueryBuilder
    {
        public QueryTree ReturnFor_BuildQuery { get; set; }
        public string LastQueryPassedTo_BuildQuery { get; set; }
        public int CountOfCallsTo_BuildQuery { get; set; }
        public QueryTree BuildQuery(string query)
        {
            CountOfCallsTo_BuildQuery++;
            LastQueryPassedTo_BuildQuery = query;
            return ReturnFor_BuildQuery;
        }
    }
}