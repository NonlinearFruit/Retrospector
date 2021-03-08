using System;
using System.Collections.Generic;
using System.Linq;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Search
{
    public class BinaryOperator : IOperator
    {
        public OperatorType OperatorType { get; }
        private readonly string _syntaxOp;

        public BinaryOperator(OperatorType type, string syntaxOp)
        {
            OperatorType = type;
            _syntaxOp = syntaxOp;
        }

        public IEnumerable<string> Parse(string query)
        {
            if (!query.Contains(_syntaxOp))
                return Array.Empty<string>();

            return query
                .Trim()
                .Split(new[] {_syntaxOp}, StringSplitOptions.RemoveEmptyEntries)
                .Select(result => result.Trim());
        }
    }
}