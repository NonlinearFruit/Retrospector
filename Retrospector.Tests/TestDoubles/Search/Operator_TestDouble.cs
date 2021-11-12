using System;
using System.Collections.Generic;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Tests.TestDoubles.Search
{
    public class Operator_TestDouble : IOperator
    {
        public OperatorType OperatorType { get; set; }

        public Func<string, IEnumerable<string>> ReturnFor_Parse { get; set; }
        public string LastQueryPassedTo_Parse { get; set; }
        public int CountOfCallsTo_Parse { get; set; }
        public virtual IEnumerable<string> Parse(string query)
        {
            CountOfCallsTo_Parse++;
            LastQueryPassedTo_Parse = query;
            return ReturnFor_Parse?.Invoke(query);
        }
    }
}