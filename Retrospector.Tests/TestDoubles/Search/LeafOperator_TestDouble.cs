using Optional;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Tests.TestDoubles.Search
{
    public class LeafOperator_TestDouble : ILeafOperator
    {
        public Option<QueryLeaf> ReturnFor_Parse { get; set; }
        public string LastQueryPassedTo_Parse { get; set; }
        public int CountOfCallsTo_Parse { get; set; }
        public Option<QueryLeaf> Parse(string query)
        {
            CountOfCallsTo_Parse++;
            LastQueryPassedTo_Parse = query;
            return ReturnFor_Parse;
        }
    }
}