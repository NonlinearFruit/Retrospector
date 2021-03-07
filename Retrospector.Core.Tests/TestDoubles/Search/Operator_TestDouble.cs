using System;
using System.Collections.Generic;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Tests.TestDoubles.Search
{
    public class Operator_TestDouble : IOperator
    {
        public OperatorType OperatorType { get; set; }

        public Func<string, IEnumerable<string>> ReturnFor_Parse { get; set; }
        public string LastQueryPassedTo_Parse { get; set; }
        public int CountOf_Parse_Calls { get; set; }
        public virtual IEnumerable<string> Parse(string query)
        {
            CountOf_Parse_Calls++;
            LastQueryPassedTo_Parse = query;
            return ReturnFor_Parse?.Invoke(query);
        }
    }
}