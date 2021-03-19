using System.Collections.Generic;

namespace Retrospector.Setup
{
    public class Configuration
    {
        public string ConnectionString { get; set; }
        public string LogFile { get; set; }
        public string DefaultUser { get; set; }
        public int DefaultRating { get; set; }
        public string GithubUser { get; set; }
        public ICollection<string> Categories { get; set; }
        public ICollection<string> Factoids { get; set; }
    }
}