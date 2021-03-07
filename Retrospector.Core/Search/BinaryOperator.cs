using System;
using System.Collections.Generic;
using System.Linq;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search
{
    public class BinaryOperator : IOperator
    {
        public OperatorType OperatorType { get; }
        private string _syntaxOp;

        public BinaryOperator(OperatorType type, string syntaxOp)
        {
            OperatorType = type;
            _syntaxOp = syntaxOp;
        }

        public IEnumerable<string> Parse(string query)
        {
            if (!query.Contains(_syntaxOp))
                return new string[0];

            return query
                .Trim()
                .Split(new[] {_syntaxOp}, StringSplitOptions.RemoveEmptyEntries)
                .Select(result => result.Trim());
        }
    }
}