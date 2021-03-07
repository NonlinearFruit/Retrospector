using Optional;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Tests.TestDoubles.Search
{
    public class LeafOperator_TestDouble : ILeafOperator
    {
        public Option<QueryLeaf> ReturnFor_Parse { get; set; }
        public string LastQueryPassedTo_Parse { get; set; }
        public int CountOf_Parse_Calls { get; set; }
        public Option<QueryLeaf> Parse(string query)
        {
            CountOf_Parse_Calls++;
            LastQueryPassedTo_Parse = query;
            return ReturnFor_Parse;
        }
    }
}