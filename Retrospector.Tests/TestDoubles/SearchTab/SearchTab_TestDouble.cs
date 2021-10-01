using System.Diagnostics.CodeAnalysis;
using Retrospector.SearchTab.Interfaces;

namespace Retrospector.Tests.TestDoubles.SearchTab
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class SearchTab_TestDouble : ISearchTab
    {
        public string Header { get; }
    }
}